namespace v8 {
namespace internal {

class IC {
 public:
  // Alias the inline cache state type to make the IC code more readable.
  typedef InlineCacheState State;

  static constexpr int kMaxKeyedPolymorphism = 4;

  // Construct the IC structure with the given number of extra
  // JavaScript frames on the stack.
  IC(Isolate* isolate, Handle<FeedbackVector> vector, FeedbackSlot slot,
     FeedbackSlotKind kind);
  virtual ~IC() = default;

  State state() const { return state_; }
  inline Address address() const;

  // Compute the current IC state based on the target stub, receiver and name.
  void UpdateState(Handle<Object> receiver, Handle<Object> name);

  bool RecomputeHandlerForName(Handle<Object> name);
  void MarkRecomputeHandler(Handle<Object> name) {
    DCHECK(RecomputeHandlerForName(name));
    old_state_ = state_;
    state_ = RECOMPUTE_HANDLER;
  }

  bool IsAnyHas() const { return IsKeyedHasIC(); }
  bool IsAnyLoad() const {
    return IsLoadIC() || IsLoadGlobalIC() || IsKeyedLoadIC();
  }
  bool IsAnyStore() const {
    return IsStoreIC() || IsStoreOwnIC() || IsStoreGlobalIC() ||
           IsKeyedStoreIC() || IsStoreInArrayLiteralICKind(kind());
  }

  static inline bool IsHandler(MaybeObject object);

  // Nofity the IC system that a feedback has changed.
  static void OnFeedbackChanged(Isolate* isolate, FeedbackVector vector,
                                FeedbackSlot slot, JSFunction host_function,
                                const char* reason);

  static void OnFeedbackChanged(Isolate* isolate, FeedbackNexus* nexus,
                                JSFunction host_function, const char* reason);

 protected:
  Address fp() const { return fp_; }
  Address pc() const { return *pc_address_; }

  void set_slow_stub_reason(const char* reason) { slow_stub_reason_ = reason; }

  Isolate* isolate() const { return isolate_; }

  // Get the caller function object.
  JSFunction GetHostFunction() const;

  inline bool AddressIsDeoptimizedCode() const;
  inline static bool AddressIsDeoptimizedCode(Isolate* isolate,
                                              Address address);

  bool is_vector_set() { return vector_set_; }
  inline bool vector_needs_update();

  // Configure for most states.
  bool ConfigureVectorState(IC::State new_state, Handle<Object> key);
  // Configure the vector for PREMONOMORPHIC.
  void ConfigureVectorState(Handle<Map> map);
  // Configure the vector for MONOMORPHIC.
  void ConfigureVectorState(Handle<Name> name, Handle<Map> map,
                            Handle<Object> handler);
  void ConfigureVectorState(Handle<Name> name, Handle<Map> map,
                            const MaybeObjectHandle& handler);
  // Configure the vector for POLYMORPHIC.
  void ConfigureVectorState(Handle<Name> name, MapHandles const& maps,
                            MaybeObjectHandles* handlers);

  char TransitionMarkFromState(IC::State state);
  void TraceIC(const char* type, Handle<Object> name);
  void TraceIC(const char* type, Handle<Object> name, State old_state,
               State new_state);

  MaybeHandle<Object> TypeError(MessageTemplate, Handle<Object> object,
                                Handle<Object> key);
  MaybeHandle<Object> ReferenceError(Handle<Name> name);

  void TraceHandlerCacheHitStats(LookupIterator* lookup);

  void UpdateMonomorphicIC(const MaybeObjectHandle& handler, Handle<Name> name);
  bool UpdatePolymorphicIC(Handle<Name> name, const MaybeObjectHandle& handler);
  void UpdateMegamorphicCache(Handle<Map> map, Handle<Name> name,
                              const MaybeObjectHandle& handler);

  StubCache* stub_cache();

  void CopyICToMegamorphicCache(Handle<Name> name);
  bool IsTransitionOfMonomorphicTarget(Map source_map, Map target_map);
  void PatchCache(Handle<Name> name, Handle<Object> handler);
  void PatchCache(Handle<Name> name, const MaybeObjectHandle& handler);
  FeedbackSlotKind kind() const { return kind_; }
  bool IsGlobalIC() const { return IsLoadGlobalIC() || IsStoreGlobalIC(); }
  bool IsLoadIC() const { return IsLoadICKind(kind_); }
  bool IsLoadGlobalIC() const { return IsLoadGlobalICKind(kind_); }
  bool IsKeyedLoadIC() const { return IsKeyedLoadICKind(kind_); }
  bool IsStoreGlobalIC() const { return IsStoreGlobalICKind(kind_); }
  bool IsStoreIC() const { return IsStoreICKind(kind_); }
  bool IsStoreOwnIC() const { return IsStoreOwnICKind(kind_); }
  bool IsKeyedStoreIC() const { return IsKeyedStoreICKind(kind_); }
  bool IsKeyedHasIC() const { return IsKeyedHasICKind(kind_); }
  bool is_keyed() const {
    return IsKeyedLoadIC() || IsKeyedStoreIC() ||
           IsStoreInArrayLiteralICKind(kind_) || IsKeyedHasIC();
  }
  bool ShouldRecomputeHandler(Handle<String> name);

  Handle<Map> receiver_map() { return receiver_map_; }
  inline void update_receiver_map(Handle<Object> receiver);

  void TargetMaps(MapHandles* list) {
    FindTargetMaps();
    for (Handle<Map> map : target_maps_) {
      list->push_back(map);
    }
  }

  Map FirstTargetMap() {
    FindTargetMaps();
    return !target_maps_.empty() ? *target_maps_[0] : Map();
  }

  State saved_state() const {
    return state() == RECOMPUTE_HANDLER ? old_state_ : state();
  }

  const FeedbackNexus* nexus() const { return &nexus_; }
  FeedbackNexus* nexus() { return &nexus_; }

 private:
  inline Address constant_pool() const;
  inline Address raw_constant_pool() const;

  void FindTargetMaps() {
    if (target_maps_set_) return;
    target_maps_set_ = true;
    nexus()->ExtractMaps(&target_maps_);
  }

  // Frame pointer for the frame that uses (calls) the IC.
  Address fp_;

  // All access to the program counter and constant pool of an IC structure is
  // indirect to make the code GC safe. This feature is crucial since
  // GetProperty and SetProperty are called and they in turn might
  // invoke the garbage collector.
  Address* pc_address_;

  // The constant pool of the code which originally called the IC (which might
  // be for the breakpointed copy of the original code).
  Address* constant_pool_address_;

  Isolate* isolate_;

  bool vector_set_;
  State old_state_;  // For saving if we marked as prototype failure.
  State state_;
  FeedbackSlotKind kind_;
  Handle<Map> receiver_map_;
  MaybeObjectHandle maybe_handler_;

  MapHandles target_maps_;
  bool target_maps_set_;

  const char* slow_stub_reason_;

  FeedbackNexus nexus_;

  DISALLOW_IMPLICIT_CONSTRUCTORS(IC);
};

class _StoreIC : public IC {
private:
    MaybeObjectHandle ComputeHandler(LookupIterator* lookup);

protected:
    void UpdateCaches(LookupIterator* lookup, Handle<Object> value,
                    StoreOrigin store_origin);

public:
    V8_WARN_UNUSED_RESULT MaybeHandle<Object> Store(
      Handle<Object> object, Handle<Name> name, Handle<Object> value,
      StoreOrigin store_origin = StoreOrigin::kNamed);
    bool LookupForWrite(LookupIterator* it, Handle<Object> value,
                      StoreOrigin store_origin);
};

class StoreIC : public _StoreIC {
private:
    MaybeObjectHandle ComputeHandler(LookupIterator* lookup);

protected:
    virtual Handle<Code> slow_stub() const {
    // All StoreICs share the same slow stub.
    return BUILTIN_CODE(isolate(), KeyedStoreIC_Slow);
  }

public:
    StoreIC(Isolate* isolate, Handle<FeedbackVector> vector, FeedbackSlot slot,
          FeedbackSlotKind kind)
      : IC(isolate, vector, slot, kind) {
    DCHECK(IsAnyStore());
  }
};

}
}
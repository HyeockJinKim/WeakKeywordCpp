
class BacktraceOutputHandler {
 public:
  virtual void HandleOutput(const char* output) = 0;

 protected:
  virtual ~BacktraceOutputHandler() = default;
};


class PrintBacktraceOutputHandler : public BacktraceOutputHandler {
 public:
  PrintBacktraceOutputHandler() = default;

  void HandleOutput(const char* output) override {
    // NOTE: This code MUST be async-signal safe (it's used by in-process
    // stack dumping signal handler). NO malloc or stdio is allowed here.
    PrintToStderr(output);
  }

 private:
  DISALLOW_COPY_AND_ASSIGN(PrintBacktraceOutputHandler);
};


class StreamBacktraceOutputHandler : public BacktraceOutputHandler {
 public:
  explicit StreamBacktraceOutputHandler(std::ostream* os) : os_(os) {}

  void HandleOutput(const char* output) override { (*os_) << output; }

 private:
  std::ostream* os_;

  DISALLOW_COPY_AND_ASSIGN(StreamBacktraceOutputHandler);
};
class A {
};

class _B : public A {
private:
    void f() {}
};

class B : public _B {
public:
    virtual void f() {}
};

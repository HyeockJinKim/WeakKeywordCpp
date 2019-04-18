class A {
};

class _B : public A {};

class B : public _B {
public:
    virtual void f() {}
};

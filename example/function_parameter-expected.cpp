class A {
};

class _B : public A {
private:
    DISALLOW(B);
    void func() {
    a();
    }
};

class B : public _B {
private:
    DISALLOW(B);

public:
    virtual void func() {
    a();
    }
};
class A {
};

class _B : public A {
private:
    DISALLOW(_B);
    void func();
};

class B : public _B {
private:
    DISALLOW(B);
public:
    virtual void func();
};
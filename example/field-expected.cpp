class A {
};

class _B : public A {
private:
    int c;
    int e;
    int b;
    int d;
    int a;
    void f();
    void func();
};

class B : public _B {
private:
    int c;
    int e;
    int b;
    int d;
    int a;
    void f();

public:
    virtual void func();
};
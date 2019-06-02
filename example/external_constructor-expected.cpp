namespace base {
class A {
public:
    A(int a, int b);
};

class _B : public A {
private:
    int c;
    int pc_;
    void f();

public:
    _B(int a, int b) : A(a, b) {}
};

class B : public _B {
private:
    int c;
    int pc_;

public:
    B(int a, int b);
    virtual void f();
};

}

namespace base {
A::A(int a, int b) {

}

B::B(int a, int b)
     : _B(a, b),
      c(0),
      pc_(3) {}

void _B::f() {
int a = 1;
}

void B::f() {
int a = 1;
}
}

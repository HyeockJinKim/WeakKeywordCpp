namespace base {
class A {
public:
    A(int a, int b);
};

class B : public A {
private:
    int pc_;
    int c;
public:
    B(int a, int b);
    virtual void f();
};

}

namespace base {
A::A(int a, int b) {

}

B::B(int a, int b)
     : A(a, b),
      c(0),
      pc_(3) {}

void B::f() {
int a = 1;
}
}

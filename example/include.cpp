#include <iostream>
#include "base.cpp"

class C : public A {
public:
    virtual void f() {}
};

int main() {
    A* a = new A;
    C* b = static_cast<C*>(a);
//    b->f();
}

#include <iostream>
#include "base.cpp"

class _B : public A {};

class B : public _B {
    virtual void f() {}
};

int main() {
    A* a = new A;
    B* b = static_cast<_B*>(a);
    b->f();
}

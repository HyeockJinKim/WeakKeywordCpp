#include <iostream>
#include "base.cpp"

class B : public A {
    virtual void f() {}
};

int main() {
    A* a = new A;
    B* b = static_cast<B*>(a);
    b->f();
}

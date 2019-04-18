#include <iostream>
#include "base.h"

class _C : public A {};

class C : public _C {
public:
    virtual void f() {}
};

int main() {
    A* a = new A;
    C* b = static_cast<_C*>(a);
//    b->f();
}

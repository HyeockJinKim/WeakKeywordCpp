#include <iostream>
#include "base.h"

class C : public A {
public:
    virtual void f() {}
};

int main() {
    A* a = new A;
    C* b = static_cast<C*>(a);
//    b->f();
}

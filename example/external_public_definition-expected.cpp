#include <iostream>
namespace base{
class A {
};

class _B : public A {
private:
    int num;
    void g();

public:
    void func();
};

class B : public _B {
private:
    int num;
    void g();

public:
    virtual void f();
};

}

namespace base{
void _B::g() {
    std::cout << "B" << std::endl;
}

void B::g() {
    std::cout << "B" << std::endl;
}

void _B::func() {
    std::cout << "Hello World!" << std::endl;
}

void B::f() {
    std::cout << this->num << std::endl;
}

}

int main() {
    A* a = new A;
    B* b = static_cast<base::_B*>(a);
//    b->f();
}

#include <iostream>
namespace base{
class A {
};

class B : public A {
private:
    int num;
    void g();
public:
    void func();
    virtual void f();
};

}

namespace base{
void B::g() {
    std::cout << "B" << std::endl;
}

void B::func() {
    std::cout << "Hello World!" << std::endl;
}

void B::f() {
    std::cout << this->num << std::endl;
}

}

int main() {
    A* a = new A;
    B* b = static_cast<base::B*>(a);
//    b->f();
}

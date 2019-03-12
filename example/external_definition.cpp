#include <iostream>

class A {
};

class B : public A {
private:
    int num;
    void g();
public:
    virtual void f();
};

void B::g() {
    std::cout << "B" << std::endl;
}

void B::f() {
    std::cout << this->num << std::endl;
}

int main() {
    A* a = new A;
    B* b = static_cast<B*>(a);
//    b->f();
}

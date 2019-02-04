#include <iostream>

class A {
};

class _B : public A {
private:
    int num;
    void g();
};

class B : public _B {
private:
    int num;
    void g();

public:
    virtual void f();
};

void _B::g() {
    std::cout << "B" << std::endl;
}

void B::g() {
    std::cout << "B" << std::endl;
}

void B::f() {
    std::cout << this->num << std::endl;
}

int main() {
    A* a = new A;
    B* b = static_cast<_B*>(a);
    b->f();
}

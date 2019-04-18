#include <iostream>

class A {
private:
    int num;
    void g();
public:
    virtual void f();
};

class B : public A {

};

void A::g() {
    std::cout << "B" << std::endl;
}

void A::f() {
    std::cout << this->num << std::endl;
}

int main() {
    A* a = new A;
    B* b = static_cast<B*>(a);
//    b->f();
}

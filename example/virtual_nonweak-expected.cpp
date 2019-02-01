#include <iostream>
using namespace std;

class A {
public:
    virtual void func() {
    }
};

class B : public A {
public:
    virtual void func() {
    cout << "B" << endl;
    }
};

int main() {
    A* a = new A;
    B* b = static_cast<B*>(a);
    b->func();
}
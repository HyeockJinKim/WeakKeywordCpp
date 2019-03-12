#include <iostream>
using namespace std;

class A {
public:
    virtual void f() {
        cout << "A" << endl;
    }
};

class B {
public:
    virtual void g() {
        cout << "B" << endl;
    }
};

class C : public A, public B {
public:
    int c;
    virtual void h() {
        cout << "C";
    }
};

int main() {
    A* a = new A;
    C* c = static_cast<C*>(a);
//    c->h();
}
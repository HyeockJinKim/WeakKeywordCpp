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

class _C : public A, public B {
private:
    void f() {
        cout << "A" << endl;
    }
    void g() {
        cout << "B" << endl;
    }
    void h() {
        cout << "C";
    }

public:
    int c;
};

class C : public _C {
public:
    virtual void h() {
        cout << "C";
    }
};

int main() {
    A* a = new A;
    C* c = static_cast<_C*>(a);
//    c->h();
}
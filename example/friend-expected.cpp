#include <iostream>

using namespace std;

class A {
private:
    int val;
    void func() {
        cout << "A" << endl;
    }
friend class _C;
friend class C;
};

class B {
};

class _C : public B {
private:
    void f() {
    }

public:
    void useA(A* a) {
        a->func();
    }
};

class C : public _C {
public:
    virtual void f() {
    }
};

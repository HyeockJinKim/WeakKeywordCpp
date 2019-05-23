#include <iostream>

using namespace std;

class A {
private:
    int val;
    void func() {
        cout << "A" << endl;
    }
friend class C;
};

class B {
};

class C : public B {
public:
    void useA(A* a) {
        a->func();
    }
    virtual void f() {
    }
};

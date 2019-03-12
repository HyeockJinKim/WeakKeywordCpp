class A {
};

class _B : public A {
public:
    void f(int a, int b);
};

class B : public _B {
public:
    virtual void f();
};

void B::f() {
}

void _B::f(int a, int b) {
    int c = a+b;
}

int main() {
    A* a = new A;
    B* b = static_cast<_B*>(a);
//    b->f(1, 2);
}

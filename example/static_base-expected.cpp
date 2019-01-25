class A {
};

class _B : public A {
public:
    void g() {}
    void h() {}
};

class B : public _B {
public:
    virtual void f() {}
};

int main() {
    A* a = new A;
    B* b = static_cast<_B*>(a);
    b->f();
}

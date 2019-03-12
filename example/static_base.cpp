class A {
};

class B : public A {
public:
    void g() {}
    void h() {}
    virtual void f() {}
};

int main() {
    A* a = new A;
    B* b = static_cast<B*>(a);
//    b->f();
}

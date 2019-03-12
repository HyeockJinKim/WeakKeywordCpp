class A {
};

class B : public A {
public:
    B() {}
    virtual void f() {}
};

int main() {
    A* a = new A;
    B* b = static_cast<B*>(a);
//    b->f();
}

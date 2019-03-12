namespace base {
class A {
};

class _B : public A {};

class B : public _B {
    virtual void f() {}
};

}

int main() {
    base::A* a = new base::a;
    base::B* b = static_cast<base::_B*>(a);
    b->f();
}

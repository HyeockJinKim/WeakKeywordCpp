namespace base {
class A {
};

class B : public A {
    virtual void f() {}
};

}

int main() {
    base::A* a = new base::a;
    base::B* b = static_cast<base::B*>(a);
    b->f();
}

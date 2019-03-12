namespace base {
class A {
};

class B : public A {
public:
    virtual void f() {}
};

}

int main() {
    base::A* a = new base::A;
    base::B* b = static_cast<base::B*>(a);
//    b->f();
}

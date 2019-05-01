namespace base {
class A {
};

class _B : public A {
private:
    void f() {}
};

class B : public _B {
public:
    virtual void f() {}
};

}

int main() {
    base::A* a = new base::A;
    base::B* b = static_cast<base::_B*>(a);
//    b->f();
}

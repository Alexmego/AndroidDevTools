# C++ 二级指针解析
~~~c++
int c=10; 

const int *b=&c; 

const int *test=&c;

const int * const * aa = &b;
~~~
---
解析：
b 是一级指针，指向c。同时，const 在* 之前，说明b 指向常量(虽然c 是变量，这里把c当作常量使用，b 指向了c ，说明 不可以通过*b 修改c 的值，因为被const修饰)，但是b 指针 可以被重新赋值。
-----
~~~c++
int d = 20;
// 这行代码ok
b=&d ;
// 这行代码是错误的 
*b =d;

~~~
--------

aa 是二级指针，指向b 。 这里有两个const，比较复杂。下面是个人理解过程，不保证过程是对的，只保证实践结果正确.  

上面的aa 指针是一个变量，不是constant ，虽然有两个const修饰。
无法通过*aa 操作，\*aa  获得一级指针，无法对一级指针进行赋值，因为二级指针aa被const 修饰，表示二级指针指向一个常量。\*aa 就是一个常量，不可以对它赋值。
-----------
# 指针中的const 修饰符总结
>  在 * 之前的const ，表示此指针指向一个常量(指向常量的指针也可以指向变量，但是指向变量的指针无法指向常量),是一个常量指针，重点是指针，常量是修饰符；在* 之后的const ，表示此指针是一个指针常量,重点是常量，指针是修饰符。 在* 之间的const ，表示指向的次级指针是常量，不可以对此次级指针修改

~~~c++
const int *const* a1;
const int ** const;
~~~
------------
+ a1 是变量，指向的一级指针被const 修饰，*a1不可以被赋值
+ a2 是常量，并且指向的一级指针被const 修饰，*a1不可以被赋值

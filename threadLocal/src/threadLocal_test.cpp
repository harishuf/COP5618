#include "threadLocal.h"

using namespace cop5618;
int test_case1(){
	threadLocal<int> t1;
	threadLocal<int> t2;
		try{
	t1.set(10);
	return t2.get()==10?0:1;
	}
catch(std::out_of_range &e){
	//std::cout<<"trying to get values when none are set";
}
return 0;
}

int test_case2(){
	threadLocal<int> t3;
	try{
		t3.get();
		t3.get();
		return 1;
	}
	catch(std::out_of_range &e){
		//std::cout<<"bad multiple reads";
	}
	return 0;
}

int test_case3(){
	threadLocal<int> t4;
	try{
		t4.set(100);
		t4.remove();
		t4.get();
		return 1;
	}
	catch(std::out_of_range &e){
		//std::cout<<"attempting a read after deletion";
	}
	return 0;
	}
	
void doParalell(){
	test_case1()+
	test_case2()+
	test_case3();
}
int test_case4(){
	int i=0;
	for(i=0;i<10;i++){
		std::thread a(doParalell);
		a.join();
	}
	return 0;
}


extern int test_threadLocal(){
	int err;
	err+=test_case1();
	err+=test_case2();
	err+=test_case3();
	err+=test_case4();
	std::cout<<"Total number of Errors in test cases is : "<< err;
	return err;
}



/*
 * threadLocal.h
 *  Start with this and add what is necessary
 */
#ifndef THREADLOCAL_H_
#define THREADLOCAL_H_
#include <iostream>
#include<map>
#include<thread>
#include <mutex>

//std::mutex cout_mutex1;

namespace cop5618 {
template <typename T>
class threadLocal {
public:
	threadLocal();
	~threadLocal();

	//disable copy, assign, move, and move assign constructors
	 threadLocal(const threadLocal&)=delete;
	 threadLocal& operator=(const threadLocal&)=delete;
	 threadLocal(threadLocal&&)=delete;
	 threadLocal& operator=(const threadLocal&&)=delete;

	 /**
	 * Returns the current thread's value.
	 * If no value has been previously set by this
	 * thread, an out_of_range exception is thrown.
	 */
	const T& get() const;
    void set(T val);
    void remove();
    template <typename U>
	friend std::ostream& operator<< (std::ostream& os, const threadLocal<U>& obj);

private:
std::map<std::thread::id,T> my_map;
};


template <typename T>
threadLocal<T>::threadLocal(){
	
}
	
template <typename T>
threadLocal<T>::~threadLocal(){
	
}

template <typename T>
const T& threadLocal<T>::get() const{
	T o;
	auto iter= my_map.find(std::this_thread::get_id());
	if(iter!=my_map.end()){
		return iter->second;
	}else {
		throw std::out_of_range("out of range");
	}
}

template <typename T>
void threadLocal<T>::remove() {
//std::lock_guard<std::mutex> lock(cout_mutex1);
	my_map.erase(std::this_thread::get_id());
}

template <typename T>
void threadLocal<T>::set(T val){
//std::lock_guard<std::mutex> lock(cout_mutex1);
my_map.insert({std::this_thread::get_id(),val});
}
} /* namespace cop5618 */

#endif /* THREADLOCAL_H_ */
	





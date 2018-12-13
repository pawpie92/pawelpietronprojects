#include "DungeonShared.h"

countDownTimer::countDownTimer(int m, int s) {
	min = m;
	sec = s;
}

int countDownTimer::getMinutes() {
	return min;
}
int countDownTimer::getSeconds() {
	return sec;
}

void countDownTimer::decrementMinute() {
	min -= 1;
	sec = 59;
}
void countDownTimer::decrementSecond() {
	sec -= 1;
}
void countDownTimer::addMinute() {
	min += 1;
	//accomodate for timer's delay
	sec += 1;
}
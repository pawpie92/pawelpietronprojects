#pragma once
#include "DankestConstants.h"

#include <windows.h> // We need to include this for "console" functions.
#include <algorithm>
#include <vector>
#include <memory>
#include <iostream>
#include <iomanip>
#include <ctime>
#include <thread>

using namespace std;

class DungeonLevel;
class DungeonObj;
class PlayerObj;
class countDownTimer;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//~						Global Dungeon Utility					~//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

/////////////////////////////////////////////////
// DankestDungeonMain.cpp
/**
 * global state tracking:
 *
 * these objects will track the state of objects on the game board
 */

extern unique_ptr<countDownTimer> gTimer;
extern PlayerObj gPlayer;
extern DungeonLevel* pCurrentDungeon;
extern DungeonLevel gDungeon1;
extern unsigned int gCurrentLevel;

void DrawMaze( DungeonLevel& dungeon, const PlayerObj& player );
bool ProcessInput( DungeonLevel& dungeon, PlayerObj& player );
void DisplayTime();
void DisplayLives();
void nextLevel();
void mapReset();

////////////////////////////////////////////////
// DungeonLevel.cpp
extern vector<DungeonLevel> gDungeonLevels;

void init_dungeons();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//~					END Global Dungeon Utility					~// 
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

class countDownTimer 
{
private:
	int min;
	int sec;

public:
	countDownTimer(int m, int s);
	int getMinutes();
	int getSeconds();
	void addMinute();
	void decrementSecond();
	void decrementMinute();

};

class DungeonLevel
{
private:
	short iHeight;
	short iWidth;
	int index;
	COORD mPlayerSpawn;
	vector<DungeonObj> arrObjects;
	vector<CHAR_INFO> screenBuffer;

public:
	DungeonLevel( short width, short height, int i);
	DungeonLevel(const DungeonLevel& previous);
	DungeonLevel( const char* filename );
	DungeonLevel();

	vector<DungeonObj>& getObjectsArr() { return arrObjects; }
	const vector<DungeonObj>& getObjectsArr() const { return arrObjects; }

	vector<CHAR_INFO>& getScreenBuffer() { return screenBuffer; }
	const vector<CHAR_INFO>& getScreenBuffer() const { return screenBuffer; }

	short getWidth() const { return iWidth; }
	short getHeight() const { return iHeight; }
	int  getIndex() { return index; }
	void setSize( short width, short height );
	
	void InitFromFile( const char* filename );
	
	void clear();
	void resetToStart();

	void addBorder();

	const COORD& getEntranceCoord() const { return mPlayerSpawn; }
	void setEntrance( short x, short y ) { mPlayerSpawn = {x,y}; }

	void addObj( short x, short y, DungeonSpriteColor color, DungeonSprite sprite );
	void addPotion( short x, short y, PotionType potion );
	void addExit( short x, short y ) { addObj( x, y, color_EXIT, sprite_EXIT ); }
	void addDoor( short x, short y ) { addObj( x, y, color_DOOR, sprite_DOOR ); }
	void addWall( short x, short y ) { addObj( x, y, color_WALL, sprite_WALL ); }
	void addTrap( short x, short y ) { addObj( x, y, color_TRAP, sprite_TRAP ); }
};


class DungeonObj
{
protected:
	COORD mPosition;
	DungeonSpriteColor iColor;
	DungeonSprite cSprite;
	bool bVisible;

	DungeonObj() = delete;

public:
	DungeonObj( short xPos, short yPos, DungeonSpriteColor color, DungeonSprite sprite );

	void setVisible( bool visible ) { bVisible = visible; }
	bool isVisible() const { return bVisible; }
	void setX(short x) { mPosition.X = x; }
	void setY(short y) { mPosition.Y = y; }
	short getX() const { return mPosition.X; }
	short getY() const { return mPosition.Y; }
	DungeonSpriteColor getColor() const { return iColor; }
	DungeonSprite getSprite() const { return cSprite; }
	PotionType getPotionType();
};


class PlayerObj : public DungeonObj
{
private:
	bool bPotion = false;
	int mLives;

	void FirePotion();
	void FlyingPotion();
public:
	PlayerObj();
	PlayerObj( int x, int y, DungeonSpriteType dir = sprite_PLAYER_RIGHT );

	bool hasPotion() const { return bPotion; }

	COORD& getPosition() { return mPosition; }
	const COORD& getPosition() const { return mPosition; }
	COORD getFrontPosition( int offset = 1 ) const;

	void setDirection( DungeonSpriteType dir ) { cSprite = dir; }
	DungeonSprite getDirection() const { return cSprite; }

	int getLives() const { return mLives; }
	void setLives(int numLives) { mLives = numLives; }
	void loseLife() { mLives -= 1; }
	void addLife() { ++mLives; }

	void pickUpPotion( PotionType potion );
	void usePotion();

	void Move( DungeonSpriteType direction );

	void resetToStart();
};

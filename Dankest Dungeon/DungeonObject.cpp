
#include "DungeonShared.h"

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//~								DungeonObj								~// 
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

DungeonObj::DungeonObj( short xPos, short yPos, DungeonSpriteColor color, DungeonSprite sprite )
	: mPosition{xPos, yPos}
	, iColor( color )
	, cSprite( sprite )
	, bVisible( true )
{
}


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//~								PlayerObj								~// 
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

PlayerObj::PlayerObj()
	: DungeonObj( 0, 0, color_PLAYER_NO_POTION, sprite_PLAYER_RIGHT )
{
}
PlayerObj::PlayerObj( int x, int y, DungeonSpriteType dir )
	: DungeonObj( x, y, color_PLAYER_NO_POTION, dir )
{
}

COORD PlayerObj::getFrontPosition( int offset ) const
{
	COORD playerPos = getPosition();
	COORD frontPosition = playerPos;
	DungeonLevel& dungeon = *pCurrentDungeon;
	// determine which position is in front by offset amount
	switch ( getDirection() )
	{
	case sprite_PLAYER_RIGHT:
		frontPosition.X += offset;
		break;
	case sprite_PLAYER_LEFT:
		frontPosition.X += -offset;
		break;
	case sprite_PLAYER_UP:
		frontPosition.Y += -offset;
		break;
	case sprite_PLAYER_DOWN:
		frontPosition.Y += offset;
		break;
	default:
		break;
	}
	return frontPosition;
}

void PlayerObj::pickUpPotion( PotionType potion )
{
	bPotion = false;
	iColor = color_PLAYER_NO_POTION;

	switch ( potion )
	{
	case pt_NONE:
		break;
	case pt_TIME:
		gTimer->addMinute();
		DisplayTime();
		break;
	case pt_LIFE:
		addLife();
		DisplayLives();
		break;
	case pt_FIRE:
		bPotion = true;
		iColor = color_FIRE_POTION;
		break;
	case pt_FLYING:
		bPotion = true;
		iColor = color_FLYING_POTION;
		break;
	}
}

void PlayerObj::FlyingPotion()
{
	DungeonLevel& dungeon = *pCurrentDungeon;
	COORD frontPosition = getFrontPosition(1);
	PlayerObj& player = *this;

	auto it = find_if(dungeon.getObjectsArr().begin(), dungeon.getObjectsArr().end(),
		[&frontPosition](const DungeonObj& obj)
	{
		if (obj.getX() == frontPosition.X &&
			obj.getY() == frontPosition.Y &&
			obj.isVisible())
		{
			return true;
		}
		return false;
	});

	if (it != dungeon.getObjectsArr().end())
	{
		DungeonObj& object = *it;

		if (object.getSprite() == sprite_DOOR || object.getSprite() == sprite_WALL) {

			return;
		}
	}

	frontPosition = getFrontPosition(2);

	it = find_if(dungeon.getObjectsArr().begin(), dungeon.getObjectsArr().end(),
		[&frontPosition](const DungeonObj& obj)
	{
		if (obj.getX() == frontPosition.X &&
			obj.getY() == frontPosition.Y &&
			obj.isVisible())
		{
			return true;
		}
		return false;
	});

	if (it != dungeon.getObjectsArr().end())
	{
		DungeonObj& object = *it;

		if (object.getSprite() == sprite_DOOR || object.getSprite() == sprite_WALL) {

			return;
		}
	}

	frontPosition = getFrontPosition(3);

	it = find_if(dungeon.getObjectsArr().begin(), dungeon.getObjectsArr().end(),
		[&frontPosition](const DungeonObj& obj)
	{
		if (obj.getX() == frontPosition.X &&
			obj.getY() == frontPosition.Y &&
			obj.isVisible())
		{
			return true;
		}
		return false;
	});

	if (it != dungeon.getObjectsArr().end())
	{
		DungeonObj& object = *it;

		if (object.getSprite() == sprite_DOOR || object.getSprite() == sprite_WALL) {

			return;
		}

		else if (object.getSprite() == sprite_TRAP) {

			mPosition = frontPosition;
			loseLife();
			DrawMaze(dungeon, player);
			Sleep(500);
			mapReset();
		}

		else if (object.getSprite() == sprite_POTION) {

			mPosition = frontPosition;
			pickUpPotion(object.getPotionType());
		}


		else if (object.getSprite() == sprite_EXIT) {

			mPosition = frontPosition;
			nextLevel();
		}
	}

	else { mPosition = frontPosition; }
}

void PlayerObj::FirePotion()
{
	DungeonLevel& dungeon = *pCurrentDungeon;
	COORD frontPosition = getFrontPosition();
	PlayerObj& player = *this;
	// check for door or trap in front of us
	auto it = find_if( dungeon.getObjectsArr().begin(), dungeon.getObjectsArr().end(), 
	[&frontPosition]( const DungeonObj& obj )
	{
		if ( obj.getX() == frontPosition.X &&
			 obj.getY() == frontPosition.Y &&
			 (obj.getSprite() == sprite_DOOR || obj.getSprite() == sprite_TRAP) &&
			 obj.isVisible() )
		{
			return true;
		}
		return false;
	} );

	if ( it != dungeon.getObjectsArr().end() )
	{
		it->setVisible( false );
	}

	// add a brief animation of a potion being used
	dungeon.getObjectsArr().emplace_back( frontPosition.X, frontPosition.Y, color_FIRE_SPLASH, sprite_FIRE_SPLASH );
	DrawMaze( dungeon, player );
	Sleep( 100 );
	dungeon.getObjectsArr().pop_back();
}

void PlayerObj::Move( DungeonSpriteType direction )
{
	DungeonLevel& dungeon = *pCurrentDungeon;
	PlayerObj& player = *this;
	vector<CHAR_INFO>& screenBuffer = dungeon.getScreenBuffer();
	const COORD oldPosition = mPosition;
	COORD frontPosition;

	setDirection( direction );
	frontPosition = getFrontPosition();

	// check for a collision with a dungeon object
	auto it = find_if( dungeon.getObjectsArr().begin(), dungeon.getObjectsArr().end(), [&frontPosition]( const DungeonObj& obj )
	{
		if ( obj.getX() == frontPosition.X &&
			 obj.getY() == frontPosition.Y &&
			 obj.isVisible() )
		{
			return true;
		}
		return false;
	} );

	if ( it != dungeon.getObjectsArr().end() )
	{
		switch ( it->getSprite() )
		{
		case sprite_EXIT:
			nextLevel();
			mapReset();
			return;
		case sprite_TRAP:
			loseLife();
			mapReset();
			DisplayLives();// Update number of lives on screen
		case sprite_DOOR:
		case sprite_WALL:
			return;	// don't update position, player is blocked
		case sprite_POTION:
			it->setVisible( false );
			pickUpPotion( it->getPotionType() );
			break;
		default:
			break;
		}
	}

	// Erase the old position of the player with a space
	screenBuffer[mPosition.X + mPosition.Y * dungeon.getWidth()].Char.AsciiChar = sprite_FLOOR;
	screenBuffer[mPosition.X + mPosition.Y * dungeon.getWidth()].Attributes = color_FLOOR;
	mPosition = frontPosition;
}


void PlayerObj::resetToStart()
{
	mPosition = pCurrentDungeon->getEntranceCoord();
	cSprite   = sprite_PLAYER_RIGHT;
	bPotion   = false;
	iColor    = color_PLAYER_NO_POTION;
	setVisible( true );
}

void PlayerObj::usePotion()
{
	switch ( getPotionType() )
	{
	case pt_FIRE:
		FirePotion();
		break;
	case pt_FLYING:
		FlyingPotion();
		break;
	case pt_TIME:
		gTimer->addMinute();
		DisplayTime();
		break;
	case pt_LIFE:
		break;
	case pt_NONE:
	default:
		return;
	}


	bPotion = false;
	iColor = color_PLAYER_NO_POTION;
}

PotionType DungeonObj::getPotionType()
{
	switch ( iColor )
	{
	case color_FIRE_POTION:		return pt_FIRE;
	case color_FLYING_POTION:	return pt_FLYING;
	case color_TIME_POTION:		return pt_TIME;
	case color_LIFE_POTION:		return pt_LIFE;
	case color_PLAYER_NO_POTION:
	default:					return pt_NONE;
	}
}


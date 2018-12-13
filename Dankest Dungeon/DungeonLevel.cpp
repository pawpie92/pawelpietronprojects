#include "DungeonShared.h"
#include <fstream>
#include <string>

// GLOBALS
vector<DungeonLevel> gDungeonLevels;


DungeonLevel::DungeonLevel( short width, short height, int i)
	: iWidth( width )
	, iHeight( height )
	, index( i )
{
	screenBuffer.resize( iWidth * iHeight );
}

DungeonLevel::DungeonLevel()
	: DungeonLevel( 0, 0, 0)
{
}
DungeonLevel::DungeonLevel(const DungeonLevel& previous) {
	iHeight = previous.iHeight;
	iWidth = previous.iWidth;
	index = previous.index;
	mPlayerSpawn = previous.mPlayerSpawn;
	arrObjects = previous.arrObjects;
	screenBuffer = previous.screenBuffer;

}

DungeonLevel::DungeonLevel( const char* filename )
	: DungeonLevel( 0, 0, 0 )
{
	InitFromFile( filename );
}

void DungeonLevel::clear()
{
	screenBuffer.clear();
	screenBuffer.resize( iWidth * iHeight );
	arrObjects.clear();
}

void DungeonLevel::resetToStart()
{
	for ( DungeonObj& dObj : arrObjects )
	{
		dObj.setVisible( true );
	}
}

void DungeonLevel::addBorder()
{
	for ( int i = 0; i < iWidth; ++i )
	{
		addWall( i, iHeight - 1 );
		addWall( i, 0 );
	}
	for ( int i = 0; i < iHeight; ++i )
	{
		addWall( iWidth - 1, i );
		addWall( 0, i );
	}
}

void DungeonLevel::addObj( short x, short y, DungeonSpriteColor color, DungeonSprite sprite )
{
	arrObjects.emplace_back( x, y, color, sprite );
}

void DungeonLevel::addPotion( short x, short y, PotionType potion )
{
	switch ( potion )
	{
	case pt_FIRE:
		addObj( x, y, color_FIRE_POTION, sprite_POTION );
		break;
	case pt_FLYING:
		addObj( x, y, color_FLYING_POTION, sprite_POTION );
		break;
	case pt_TIME:
		addObj(x, y, color_TIME_POTION, sprite_POTION);
		break;
	case pt_LIFE:
		addObj( x, y, color_LIFE_POTION, sprite_POTION );
		break;
	case pt_NONE:
	default:
		break;
	}
}

void DungeonLevel::setSize( short width, short height )
{
	iWidth = width;
	iHeight = height;
	screenBuffer.resize( iWidth * iHeight );
}


void DungeonLevel::InitFromFile( const char* filename )
{
	fstream mapFile;
	mapFile.open( filename );
	string line;
	int x = 0;
	int y = 0;

	if( mapFile.is_open() )
	{
		while( getline(mapFile,line) )
		{
			// skip all comment lines
			if( line[0] == ';' )
			{
				continue;
			}

			x = 0;
			for ( auto it = line.begin(); it != line.end(); ++it )
			{
				switch ( *it )
				{
				case id_FIRE_POTION:
					addPotion( x, y, pt_FIRE );
					break;
				case id_FLYING_POTION:
					addPotion( x, y, pt_FLYING );
					break;
				case id_TIME_POTION:
					addPotion( x, y, pt_TIME );
					break;
				case id_LIFE_POTION:
					addPotion( x, y, pt_LIFE );
					break;
				case id_WALL:
					addWall( x, y );
					break;
				case id_DOOR:
					addDoor( x, y );
					break;
				case id_EXIT:
					addExit( x, y );
					break;
				case id_ENTRANCE:
					setEntrance( x, y );
					break;
				case id_TRAP:
					addTrap( x, y );
					break;
				case ' ':
					break;
				default:
					addObj( x, y, color_UI_FONT, *it );
					break;
				}
				++x;
			}
			++y;
		}
		mapFile.close();
	}

	setSize( x, y );
}

/**
 * this is where we init each dungeon level from their respective text files
 */
void init_dungeons()
{
	gDungeonLevels.emplace_back( TITLE_SCREEN );
	gDungeonLevels.emplace_back( LEVEL_TUTORIAL );
	gDungeonLevels.emplace_back( LEVEL_1 );
	gDungeonLevels.emplace_back( LEVEL_2 );
	gDungeonLevels.emplace_back( LEVEL_3 );
}

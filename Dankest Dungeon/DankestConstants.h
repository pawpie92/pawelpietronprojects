#pragma once
/**
 * DankestConstants.h
 * 
 * holds all our constants, enum, defines, etc.
 */
#include <windows.h>

typedef int DungeonColor;
enum DungeonSpriteColor : DungeonColor
{
	// foreground colors
	fg_BLACK       = 0,
	fg_DARKCYAN    = FOREGROUND_GREEN | FOREGROUND_BLUE,
	fg_DARKGREEN   = FOREGROUND_GREEN,
	fg_DARKRED     = FOREGROUND_RED,
	fg_DARKBLUE    = FOREGROUND_BLUE,
	fg_DARKMAGENTA = FOREGROUND_RED | FOREGROUND_BLUE,
	fg_DARKYELLOW  = FOREGROUND_RED | FOREGROUND_GREEN,
	fg_GRAY        = FOREGROUND_RED | FOREGROUND_GREEN | FOREGROUND_BLUE,
	fg_WHITE       = FOREGROUND_INTENSITY | FOREGROUND_RED | FOREGROUND_GREEN | FOREGROUND_BLUE,
	fg_DARKGRAY    = FOREGROUND_INTENSITY,
	fg_BLUE        = FOREGROUND_INTENSITY | FOREGROUND_BLUE,
	fg_GREEN       = FOREGROUND_INTENSITY | FOREGROUND_GREEN,
	fg_CYAN        = FOREGROUND_INTENSITY | FOREGROUND_GREEN | FOREGROUND_BLUE,
	fg_RED         = FOREGROUND_INTENSITY | FOREGROUND_RED,
	fg_MAGENTA     = FOREGROUND_INTENSITY | FOREGROUND_RED | FOREGROUND_BLUE,
	fg_YELLOW      = FOREGROUND_INTENSITY | FOREGROUND_RED | FOREGROUND_GREEN,

	// background colors
	bg_BLACK       = 0,
	bg_DARKCYAN    = BACKGROUND_GREEN | BACKGROUND_BLUE,
	bg_DARKGREEN   = FOREGROUND_GREEN,
	bg_DARKRED     = BACKGROUND_RED,
	bg_DARKBLUE    = FOREGROUND_BLUE,
	bg_DARKMAGENTA = BACKGROUND_RED | BACKGROUND_BLUE,
	bg_DARKYELLOW  = BACKGROUND_RED | BACKGROUND_GREEN,
	bg_GRAY        = BACKGROUND_RED | BACKGROUND_GREEN | BACKGROUND_BLUE,
	bg_WHITE       = BACKGROUND_INTENSITY | BACKGROUND_RED | BACKGROUND_GREEN | BACKGROUND_BLUE,
	bg_DARKGRAY    = BACKGROUND_INTENSITY,
	bg_BLUE        = BACKGROUND_INTENSITY | BACKGROUND_BLUE,
	bg_GREEN       = BACKGROUND_INTENSITY | BACKGROUND_GREEN,
	bg_CYAN        = BACKGROUND_INTENSITY | BACKGROUND_GREEN | BACKGROUND_BLUE,
	bg_RED         = BACKGROUND_INTENSITY | BACKGROUND_RED,
	bg_MAGENTA     = BACKGROUND_INTENSITY | BACKGROUND_RED | BACKGROUND_BLUE,
	bg_YELLOW      = BACKGROUND_INTENSITY | BACKGROUND_RED | BACKGROUND_GREEN,

	// preset color combos = Foreground | Background
	color_FLOOR            = bg_BLACK,
	color_UI_FONT          = fg_GRAY | color_FLOOR,
	color_WALL             = fg_DARKBLUE | bg_DARKGRAY,
	color_PLAYER_NO_POTION = fg_WHITE | color_FLOOR,
	color_FIRE_POTION      = fg_DARKRED | color_FLOOR,
	color_FLYING_POTION    = fg_DARKCYAN | color_FLOOR,
	color_TIME_POTION      = fg_YELLOW | color_FLOOR,
	color_LIFE_POTION      = fg_GREEN | color_FLOOR,
	color_FIRE_SPLASH      = fg_RED | color_FLOOR,
	color_DOOR             = fg_DARKYELLOW | color_FLOOR,
	color_EXIT             = fg_RED | bg_DARKRED,
	color_TRAP             = fg_MAGENTA | color_FLOOR,
};

typedef char DungeonSprite;
enum DungeonSpriteType : char
{
	sprite_PLAYER_UP    = '^',
	sprite_PLAYER_DOWN  = 'v',
	sprite_PLAYER_LEFT  = '<',
	sprite_PLAYER_RIGHT = '>',
	sprite_WALL         = '#',
	sprite_POTION       = '&',
	sprite_DOOR         = '@',
	sprite_EXIT         = 'X',
	sprite_FLOOR        = ' ',
	sprite_FIRE_SPLASH  = '*',
	sprite_TRAP         = '+',
};

/**
 *  for identifying potion types when reading map from file,
 *  or when using them
 */
enum PotionType : char
{
	pt_NONE   = ' ',
	pt_FIRE   = 'f',
	pt_FLYING = 'j',
	pt_TIME   = 't',
	pt_LIFE   = 'l',
};


/**
 * these characters are what are used for recognizing objects inside
 * of the level design .txt files
 */
enum DungeonObjectID : char
{
	id_FIRE_POTION   = pt_FIRE,
	id_FLYING_POTION = pt_FLYING,
	id_TIME_POTION   = pt_TIME,
	id_LIFE_POTION   = pt_LIFE,
	id_WALL          = '#',
	id_DOOR          = '@',
	id_EXIT          = 'x',
	id_ENTRANCE      = 's',
	id_TRAP          = '+',
};

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//~				LEVEL MAP FILEPATHS					~//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

#define TEMPLATE_LEVEL "DungeonLevelMaps/TemplateMap.txt"
#define TITLE_SCREEN "DungeonLevelMaps/DDTitleScreen.txt"
#define LEVEL_TUTORIAL "DungeonLevelMaps/DDTutorial.txt"
#define LEVEL_1 "DungeonLevelMaps/DDLevel1.txt"
#define LEVEL_2 "DungeonLevelMaps/DDLevel2.txt"
#define LEVEL_3 "DungeonLevelMaps/DDLevel3.txt"


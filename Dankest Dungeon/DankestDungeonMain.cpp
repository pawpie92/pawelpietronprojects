/*
 *
 * Authors of the game: Kevin Bell, Etianne Palmer-Campbell, Pawel Pietron, Allan Velednitskiy
 *
*/

#include "DungeonShared.h"

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
//~								Globals							~//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
unique_ptr<countDownTimer> gTimer;
PlayerObj gPlayer;
DungeonLevel* pCurrentDungeon;
DungeonLevel gDungeon1;
DungeonLevel tutorialDungeon;
unsigned int gCurrentLevel = 0;

bool timeIsUp = false;
bool win = false;
bool isGameOver = false;

void gameOver(DungeonLevel& dungeon, PlayerObj& player)
{
	dungeon.clear();
	player.setVisible(false);
	player.resetToStart();

	DrawMaze(dungeon, player);
	if (timeIsUp)
		cout << "Time is up! " << endl;
	else if (win)
		cout << "You won! " << endl;
	else
		cout << "You died! " << endl;
	cout << "Game Over" << endl;

	Sleep(2000);
}

void mapReset()
{
	pCurrentDungeon->resetToStart();
	gPlayer.resetToStart();
	
	DrawMaze(*pCurrentDungeon, gPlayer);
}

void DisplayTime() {

	// Timer's coordinates
	COORD timeCoord = { 27 , 2 };

	SetConsoleCursorPosition(GetStdHandle(STD_OUTPUT_HANDLE), timeCoord);
	cout << gTimer->getMinutes() << ":";
	cout << setw(2) << setfill('0') << gTimer->getSeconds() << endl;
}

void DisplayLives() {

	//Lives display
	COORD LivesCoord = { 41, 2 };

	SetConsoleCursorPosition(GetStdHandle(STD_OUTPUT_HANDLE), LivesCoord);
	cout << gPlayer.getLives() << endl;
}

void DisplayCurrentLevel() 
{
	//Display Current level
	COORD levelCoord = { 53 , 2 };
	SetConsoleCursorPosition(GetStdHandle(STD_OUTPUT_HANDLE), levelCoord);
	cout << gCurrentLevel << endl;
}
void StartTimer() {

	DisplayTime();
	DisplayLives();
	DisplayCurrentLevel();

	// Time loop
	while (1)
	{
		if (gTimer->getSeconds() == 0)
		{
			gTimer->decrementMinute();
		}
		else
		{
			gTimer->decrementSecond();
		}
		DisplayTime();

		// When timer reaches 0 seconds end game
		if ( gTimer->getMinutes() == 0 && gTimer->getSeconds() == 0 )
		{
			DisplayTime();
			timeIsUp = true;
			gameOver( *pCurrentDungeon, gPlayer );
			isGameOver = true;
		}

		Sleep(1000);
	};

}

int main()
{
	// fills up the dungeon levels with objects
	init_dungeons();

	// start tutorial
// 	pCurrentDungeon = &tutorialDungeon;

	// Begin on title screen
	pCurrentDungeon = &gDungeonLevels[gCurrentLevel];

	gPlayer.setLives( 100 );// so that user doesn't die during tutorial... probably
	gPlayer.resetToStart();

	// Set up a timer
	gTimer = std::make_unique<countDownTimer>( 2, 0 );

	SetConsoleTextAttribute( GetStdHandle( STD_OUTPUT_HANDLE ), color_UI_FONT );

	DrawMaze( *pCurrentDungeon, gPlayer );

	// play through the title and tutorial before starting timer
	while (gCurrentLevel < 2)
	{
		if ( ProcessInput( *pCurrentDungeon, gPlayer ) )
		{
			DrawMaze( *pCurrentDungeon, gPlayer );
			if(gCurrentLevel > 0)
			{
				DisplayTime();
				DisplayLives();
				DisplayCurrentLevel();
			}
		}
	}

	thread timer(StartTimer);
	gPlayer.setLives(2);

	// play through rest of game
	while ( !isGameOver )
	{
		if (ProcessInput(*pCurrentDungeon, gPlayer))
		{
			DrawMaze(*pCurrentDungeon, gPlayer);
		}

		DisplayTime();
		DisplayLives();
		DisplayCurrentLevel();

		if ( gPlayer.getLives() == 0 )
		{
			gameOver( *pCurrentDungeon, gPlayer );
			isGameOver = true;
		}
	}
	timer.detach();
	return 1;
}// main()


void DrawMaze(DungeonLevel& dungeon, const PlayerObj& player)
{
	vector<CHAR_INFO>& screenBuffer = dungeon.getScreenBuffer();
	const COORD& playerPos          = player.getPosition();
	SMALL_RECT drawRect             = { 0, 0, dungeon.getWidth() - 1, dungeon.getHeight() - 1 };
	COORD bufferSize                = { dungeon.getWidth() , dungeon.getHeight() };
	COORD bufferCoord               = { 0, 0 };
	HANDLE hOutput                  = GetStdHandle(STD_OUTPUT_HANDLE);

	// draw floor
	for (CHAR_INFO& sprite : screenBuffer)
	{
		sprite.Char.AsciiChar = sprite_FLOOR;
		sprite.Attributes = color_FLOOR;
	}

	// draw all active dungeon objects
	for (const auto& obj : dungeon.getObjectsArr())
	{
		// add to map if visible
		if (obj.isVisible())
		{
			screenBuffer[obj.getX() + obj.getY() * dungeon.getWidth()].Char.AsciiChar = obj.getSprite();
			screenBuffer[obj.getX() + obj.getY() * dungeon.getWidth()].Attributes = obj.getColor();
		}
	}

	// draw player
	screenBuffer[playerPos.X + playerPos.Y * dungeon.getWidth()].Char.AsciiChar = player.getDirection();
	screenBuffer[playerPos.X + playerPos.Y * dungeon.getWidth()].Attributes = player.getColor();

	// This draws our buffer to the screen lightning fast!
	WriteConsoleOutput(hOutput, screenBuffer.data(), bufferSize, bufferCoord, &drawRect);
}

/**
 * Advance to the next level of dungeon
 */
void nextLevel()
{
	// clear the screen
	pCurrentDungeon->clear();
	gPlayer.setVisible( false );
	DrawMaze( *pCurrentDungeon, gPlayer );

	if ( ++gCurrentLevel >= gDungeonLevels.size() )
	{
		cout << "you win!" << endl;
		Sleep( 2000 );
		exit( 0 );
	}
	else
	{
		pCurrentDungeon = &gDungeonLevels[gCurrentLevel];
		gPlayer.resetToStart();
		DrawMaze( *pCurrentDungeon, gPlayer );
	}
}


// return true if the screen needs to be updated
bool ProcessInput(DungeonLevel& dungeon, PlayerObj& player)
{
	vector<CHAR_INFO>& screenBuffer = dungeon.getScreenBuffer();
	INPUT_RECORD InputRecord;			// Here is our structure to hold the information on the input buffer (what the user does).
	DWORD Events = 0;					// We create DWORD to be compatible with ReadConsoleInput().  (Holds how many input events took place)
	HANDLE hInput;						// This will be used to query the keyboard
	bool bUpdateScreen = false;			// has anything changed?
	int bKeyDown = 0;					// This tells us if we pressed DOWN a key, not UP too

	hInput = GetStdHandle(STD_INPUT_HANDLE);

	ReadConsoleInput(hInput, &InputRecord, 1, &Events);

	// For preventing double inputs we only register key press, NOT release
	bKeyDown = InputRecord.Event.KeyEvent.bKeyDown;

	if (InputRecord.EventType == KEY_EVENT && bKeyDown)
	{
		switch (InputRecord.Event.KeyEvent.wVirtualKeyCode)
		{
		case VK_RIGHT:
			player.Move(sprite_PLAYER_RIGHT);
			bUpdateScreen = true;
			break;
		case VK_LEFT:
			player.Move(sprite_PLAYER_LEFT);
			bUpdateScreen = true;
			break;
		case VK_UP:
			player.Move(sprite_PLAYER_UP);
			bUpdateScreen = true;
			break;
		case VK_DOWN:
			player.Move(sprite_PLAYER_DOWN);
			bUpdateScreen = true;
			break;
		case VK_SPACE:
			if (player.hasPotion())
			{
				player.usePotion();
				bUpdateScreen = true;
			}
			break;
		case VK_ESCAPE:
			exit(0);
			break;
		case VK_BACK:
			mapReset();
			gPlayer.loseLife();
			break;
		default:
			break;
		}
	}

	// This clears the input buffer each time so it doesn't echo.
	FlushConsoleInputBuffer(hInput);

	return bUpdateScreen;
}


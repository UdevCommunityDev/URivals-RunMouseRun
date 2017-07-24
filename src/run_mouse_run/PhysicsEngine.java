package run_mouse_run;

class PhysicsEngine
{
    private Map levelMap;

    PhysicsEngine()
    {
        levelMap = GameManager.gameManager.getLevelGenerator().getMap();
    }

    void update()
    {
        // Check in this order, if a cat is hiding behind a cheese and mouse reach it, mouse win
        checkIsMouseOnSpecialTile();
        checkIsCatsOnSpecialTile();
    }

    void setLevelMap(Map map)
    {
        this.levelMap = map;
    }

    private void checkIsCatsOnSpecialTile()
    {
        for (Cat cat: GameManager.gameManager.getCats())
        {
            if(!cat.isAlive())
                continue;

            Tile tileCatIsStandingOn = levelMap.getTile(cat.getPosition().getPosX(), cat.getPosition().getPosY());

            switch (tileCatIsStandingOn)
            {
                case MOUSE:
                    cat.increaseTargetReachedCount();

                    for (Mouse dyingMouse: GameManager.gameManager.getMouses())
                    {
                        if(Position.comparePosition(cat.getPosition(), dyingMouse.getPosition()))
                        {
                            dyingMouse.die();
                            GameManager.gameManager.getDrawEngine().printMessage(cat.getName() + " killed " + dyingMouse.getName());
                            GameManager.gameManager.chekEndGameConditions("Cats Win", cat.getName());
                            break;
                        }
                    }

                    break;
                case MINE:
                    GameManager.gameManager.getDrawEngine().explodeMine(cat.getPosition().getPosX(), cat.getPosition().getPosY());
                    levelMap.setTile(cat.getPosition().getPosX(), cat.getPosition().getPosY(), Tile.EMPTY);
                    GameManager.gameManager.getDrawEngine().printMessage("Respawn: "+ cat + " died on a Mine");
                case WALL:
                    GameManager.gameManager.getDrawEngine().printMessage("Respawn: "+ cat + " walked into a wall");
                    cat.respawn();
                    break;
                case POWERUP_VISION:
                    cat.applyVisionPowerUp();
                    GameManager.gameManager.getLevelGenerator().spawnVisionPowerup();
                    break;
                case POWERUP_SPEED:
                    cat.applyMoveSpeedPowerup();
                    GameManager.gameManager.getLevelGenerator().spawnSpeedPowerup();
                    break;
            }

            if(tileCatIsStandingOn == Tile.POWERUP_SPEED || tileCatIsStandingOn == Tile.POWERUP_VISION
                    || tileCatIsStandingOn == Tile.EMPTY || tileCatIsStandingOn == Tile.MOUSE)
                levelMap.setTile(cat.getPosition().getPosX(), cat.getPosition().getPosY(), Tile.CAT);
        }
    }

    private void checkIsMouseOnSpecialTile()
    {
        for (Mouse mouse: GameManager.gameManager.getMouses())
        {
            if (!mouse.isAlive())
                continue;

            Tile tileMouseIsStandingOn = levelMap.getTile(mouse.getPosition().getPosX(), mouse.getPosition().getPosY());

            switch (tileMouseIsStandingOn)
            {
                case CHEESE:
                    mouse.increaseTargetReachedCount();
                    GameManager.gameManager.getDrawEngine().printMessage(mouse.getName() + " picked a cheese");
                    GameManager.gameManager.chekEndGameConditions("Mouses Win", mouse.getName());
                    break;

                case CAT:
                    for (Cat winningCat: GameManager.gameManager.getCats())
                    {
                        if(Position.comparePosition(mouse.getPosition(), winningCat.getPosition()))
                        {
                            winningCat.increaseTargetReachedCount();
                            mouse.die();
                            GameManager.gameManager.getDrawEngine().printMessage(winningCat.getName() + " killed " + mouse.getName());
                            GameManager.gameManager.chekEndGameConditions("Cats Win", winningCat.getName());
                            break;
                        }
                    }

                    break;
                case MINE:
                    GameManager.gameManager.getDrawEngine().explodeMine(mouse.getPosition().getPosX(), mouse.getPosition().getPosY());
                    levelMap.setTile(mouse.getPosition().getPosX(), mouse.getPosition().getPosY(), Tile.EMPTY);
                    GameManager.gameManager.getDrawEngine().printMessage("Respawn: "+ mouse + " died on a Mine");
                case WALL:
                    GameManager.gameManager.getDrawEngine().printMessage("Respawn: "+ mouse + " walked into a wall");
                    mouse.respawn();
                    break;
                case POWERUP_VISION:
                    mouse.applyVisionPowerUp();
                    GameManager.gameManager.getLevelGenerator().spawnVisionPowerup();
                    break;
                case POWERUP_SPEED:
                    mouse.applyMoveSpeedPowerup();
                    GameManager.gameManager.getLevelGenerator().spawnSpeedPowerup();
                    break;
            }

            if(tileMouseIsStandingOn == Tile.POWERUP_SPEED || tileMouseIsStandingOn == Tile.POWERUP_VISION
                    || tileMouseIsStandingOn == Tile.EMPTY || tileMouseIsStandingOn == Tile.CHEESE)
                levelMap.setTile(mouse.getPosition().getPosX(), mouse.getPosition().getPosY(), Tile.MOUSE);
        }
    }
}

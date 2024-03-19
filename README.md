# IS24-AM17

```mermaid
classDiagram

class PlaceableCard{
    - id : int
    - currSide : Side
    - priority : int

    + getResources() List ~ ResourceType ~
    + changeCurrSide() void
    + getCurrSide() Side
    + canPlace(availableResources : int[]) bool
    + getPoints() int
    + flipCard() void
    + getPriority() int
    + getBack() Side
    + getFront() Side
    + getRequiredResources() List ~ ResourceType ~
}

class ResourceCard{
    - points : int

    + getPoints() int
}

class GoldenCard{
    - isPointPerResource : boolean
    - pointPerResourceRes : ResourceType
    - isPointPerCoveredCorner : boolean
    - requiredResources : HashMap ~ ResourceType, Integer ~
    - points : int
    
    + isPointPerResource() boolean
    + getPointPerResourceRes() ResourceType
    + isPointPerCoveredCorner() boolean
    + getRequiredResources() HashMap ~ ResourceType, Integer ~
    + getPoints() int
}

%% class StarterCard{

%% }

class ObjectiveCard{
    - id : int

    + calculateObjectivePoints(playArea : HashMap<PlaceableCard, Position>, numOfResourcesArr : int[]) int
    + getId() int
}

class ResourcesCountObjectiveCard{
    - points : int
    - resourceType : ResourceType
    - requiredResourceCount : int
}

class TrinityObjectiveCard{
    - points : int
}

ObjectiveCard <|-- DiagonalObjectiveCard
ObjectiveCard <|-- LShapeObjectiveCard
ObjectiveCard <|-- ResourcesCountObjectiveCard
ObjectiveCard <|-- TrinityObjectiveCard

PlaceableCard <|-- ResourceCard
PlaceableCard <|-- GoldenCard
PlaceableCard <|-- StarterCard
PlaceableCard *-->"2" Side

ResourceCard "cardType"*-->"1" CardElementType
GoldenCard "cardType"*-->"1" CardElementType

class Side{
    <<abstract>>
    + getTRCorner() CardCorner
    + getBRCorner() CardCorner
    + getBLCorner() CardCorner
    + getTLCorner() CardCorner
    + getResources() List ~ ResourceType ~
}

class Back{
    - permanentResources : List ~ ResourceType ~
}

%% class Front{
    
%% }

Side <|-- Front
Side <|-- Back
Side *-->"4" CardCorner

class CardCorner{
    <<abstract>>
    + isAvailable() boolean
    + getResource() ResourceType
}

class ResourceCorner{
    - resourceType : ResourceType    
}


CardCorner <|-- VisibleCorner
CardCorner <|-- HiddenCorner
VisibleCorner <|-- ResourceCorner
VisibleCorner <|-- EmptyCorner

class VisibleCorner{
    <<abstract>>
}

class Position{
    - xPos : int
    - yPos : int

    + equals(obj : Object) boolean
    + setX(x : int) void
    + setY(y : int) void
    + getX() int
    + getY() int
}

class CardElementType{
    <<enumeration>>
    ANIMAL = 0
    PLANT = 1
    FUNGI = 2
    INSECT = 3
}

class ResourceType{
    <<enumeration>>
    ANIMAL
    PLANT
    FUNGI
    INSECT
    QUILL
    INKWELL
    MANUSCRIPT

    - value : int
    - str : String

    + getValue() int
    + toString() String
}

class PlayerModel{
    - nickname : String
    - id : int
    - hand : List ~ PlaceableCard ~

    - objectiveToChoose : List ~ ObjectiveCard ~

    - secretObjective : ObjectiveCard
    - starterCard : PlaceableCard

    - numOfResourcesArr : int[]

    - currScore : int

    - playArea : Map ~ Position, PlaceableCard ~

    - token : Token
    - firstPlayerToken : Token

    + drawCard(card : Card)
    + flipStarterCard() void
    + setStarterCard(starterCard : PlaceableCard) void
    + placeStarterCard() void
    + setObjectiveChoice(list : List) void
    + setObjectiveCard(index : int) void
    + placeCard(card : GoldenCard, pos : Position) void
    + placeCard(card : ResourceCard, pos : Position) void
    + setAsFirstPlayer() void
    + endTurn() void
    + getId() int
    + getNickname() String
}

%% class PlayerView{
%%     <<Mettere>>
%%     + setStarterCardChoice() void
%%     + flipStarterCard() void
%%     + placeStarterCard() void
%%     + placeCard(card : Card) void
%% }

class GameResources{
    <<singleton>>
    - goldenDeck : List
    - resourcesDeck : List

    - starterDeck : List

    - objectiveDeck : List

    - tokens : List

    + copyDeck(deck : List) List
}

class GameModel{
    - board : Map ~ PlayerModel, int ~

    - playersList : List ~ int ~
    - currentPlayer : int
    - firstPlayer : int

    %% - visibleResourceCards : List ~ Card ~
    %% - resourceCardDeck : List ~ Card ~

    %% - visibleGoldenCards : List ~ Card ~
    %% - goldenCardDeck : List ~ Card ~

    %% - objectiveCardDeck : List ~ ObjectiveCard ~
    %% - commonObjectives : List ~ ObjectiveCard ~

    %% - starterCardDeck : List ~ Card ~

    + getPlayerList() List ~ int ~

    + initializeDecks() void
    + shuffleAllDecks() void
    + setVisibleCards() void
    + drawResourceCards() List ~ PlaceableCard ~
    + drawGoldenCard() PlaceableCard
    + drawStarterCard() PlaceableCard
    + drawObjectiveCard() Card

    %% + flipStarterCard(player: PlayerModel)
    %% + flipCard(card : Card)
    %% + placeCard(card : Card, player : PlayerModel)
    + selectToken(token)
    + setCommonObjectives() void
    %% + setObjectiveChoice() List
    + endTurn() void
}

GameModel *-->"2" UsableCardsDeck
%% UsableCardsDeck -->"1" ResourceCardDeck
%% UsableCardsDeck -->"1" GoldenCardDeck
GameModel *-->"1" ObjectiveCardsDeck
GameModel *-->"1" StarterCardsDeck

class UsableCardsDeck{
    - deck : List ~ PlaceableCard ~
    - visibleCards : List ~ PlaceableCard ~

    + shuffleDeck() void
    + drawFromDeck() PlaceableCard
    + drawVisibleCard(index : int) PlaceableCard
    + restoreVisibleCards() void
}

class ObjectiveCardsDeck{
    - deck : List ~ Card ~
    - commonObjectives : List ~ Card ~

    + shuffleDeck() void
    + drawFromDeck() Card
    + setCommonObjectives() void
}

class StarterCardsDeck{
    - deck : List ~ PlaceableCard ~

    + shuffleDeck() void
    + drawFromDeck() PlaceableCard
}

%% class GameView{
%%     + update() void
%% }

class GameController{
    - CardIDMap : Map ~ Integer, Card ~
    - PlayerIDMap : Map ~ Integer, PlayerModel ~

    + intializeGame() void
    + flipStarterCard(playerId : int) void
    + setToken(playerId : int, token : Token)
    + placeStarterCard(playerId : int) void
    + drawResourceCard() PlaceableCard
    + drawGoldenCard() PlaceableCard
    + setCommonObjectives() void
    + setObjectiveChoice() void
    + setRandomFirstPlayer() void
    + placeCard(cardId : int, pos : Position, playerId : int) void
    + endTurn() void
}

GameController*-->"1" GameModel
%% GameController*-->"1" GameView
GameController*-->"1 ... n" PlayerModel
%% GameController*-->"1 ... n" PlayerView

class Token{
    - id :int
}

```

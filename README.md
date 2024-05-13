# IS24-AM17

Initial UML

```mermaid

classDiagram

class PlaceableCard{
    - id : int
    - elementType : CardElementType
    - priority : int
    - currSide : Side
    - front : Side
    - back : Side

    + getResources() List ~ ResourceType ~
    + getPriority() int
    + setPriority(newPriority : int) void
    + getElementType() CardElementType
    + getBack() Side
    + getFront() Side
    + getCurrSide() Side
    + flipCard() void
    + getId() int
}

class ResourceCard{
    - points : int

    + getPoints() int
}

class GoldenCard{
    - isPointPerResource : boolean
    - pointPerResourceRes : ResourceType
    - requiredResources : HashMap ~ ResourceType, Integer ~
    - points : int
    
    + isPointPerResource() boolean
    + getPointPerResourceRes() ResourceType
    + getRequiredResources() HashMap ~ ResourceType, Integer ~
    + getPoints() int
}

class ObjectiveCard{
    <<abstract>>
    - id : int

    + getId() int
    + calculateObjectivePoints(playArea : HashMap<PlaceableCard, Position>, numOfResourcesArr : int[]) int
}

class ResourcesCountObjectiveCard{
    - points : int
    - resourceType : ResourceType
    - requiredResourceCount : int
}

class TrinityObjectiveCard{
    - points : int
}

class DiagonalObjectiveCard{
    <<abstract>>
    - points : int
    - elementType: CardElementType

    # calculateObjectivePoints(playArea : HashMap~Position, PlaceableCard~, direction : int) int
}

class TLBRDiagonalObjectiveCard{
    - direction : int
}

class TRBLDiagonalObjectiveCard{
    - direction : int
}

class LShapeObjectiveCard{
    <<abstract>>
    - points : int
    - mainElementType: CardElementType
    - secondaryElementType: CardElementType
    
    # calculateObjectivePoints(playArea : HashMap~Position, PlaceableCard~, xDirection : int, yDirection : int) int
}

class TLLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

class TRLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

class BLLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

class BRLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

ObjectiveCard <|-- DiagonalObjectiveCard
ObjectiveCard <|-- LShapeObjectiveCard
ObjectiveCard <|-- ResourcesCountObjectiveCard
ObjectiveCard <|-- TrinityObjectiveCard

DiagonalObjectiveCard <|-- TLBRDiagonalObjectiveCard
DiagonalObjectiveCard <|-- TRBLDiagonalObjectiveCard

LShapeObjectiveCard <|-- TLLShapeObjectiveCard
LShapeObjectiveCard <|-- TRLShapeObjectiveCard
LShapeObjectiveCard <|-- BLLShapeObjectiveCard
LShapeObjectiveCard <|-- BRLShapeObjectiveCard

PlaceableCard <|-- ResourceCard
PlaceableCard <|-- GoldenCard
PlaceableCard <|-- StarterCard
PlaceableCard *-->"3" Side

ResourceCard "cardType"*-->"1" CardElementType
GoldenCard "cardType"*-->"1" CardElementType

class Side{
    <<abstract>>
    - BLCorner : CardCorner
    - BRCorner : CardCorner
    - TLCorner : CardCorner
    - TRCorner : CardCorner

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

class VisibleCorner{
    <<abstract>>
}

CardCorner <|-- VisibleCorner
CardCorner <|-- HiddenCorner
VisibleCorner <|-- ResourceCorner
VisibleCorner <|-- EmptyCorner

class Position{
    - xPos : int
    - yPos : int

    + getX() int
    + getY() int
}

class CardElementType{
    <<enumeration>>
    STARTER
    ANIMAL
    PLANT
    FUNGI
    INSECT
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

    + getID() int
    + getNickname() String
    + getCurrScore() int

    + placeStarterCard() void
    + placeCard(rCard : ResourceCard, pos : Position) void
    + placeCard(gCard : GoldenCard, pos : Position) void
    + calculateObjectivePoints(oCard : ObjectiveCard) void 

    %% Not implemented yet
    + drawCard(card : Card)
    + setStarterCard(starterCard : PlaceableCard) void
    + setObjectiveChoice(list : List ~ ObjectiveCard ~) void
    + setObjectiveCard(index : int) void
    + setAsFirstPlayer() void
    + endTurn() void
}

%% class PlayerView{
%%     <<Mettere>>
%%     + setStarterCardChoice() void
%%     + flipStarterCard() void
%%     + placeStarterCard() void
%%     + placeCard(card : Card) void
%% }

class GameResources{
    - goldenDeck : List ~ PlaceableCard ~
    - resourcesDeck : List ~ PlaceableCard ~

    - starterDeck : List ~ StarterCard ~

    - objectiveDeck : List ~ ObjectiveCard ~

    - tokensList : List ~ Token ~

    + getResourcesDeck() List ~ PlaceableCard ~
    + getGoldenDeck() List ~ PlaceableCard ~
    + getStarterDeck() List ~ StarterCard ~
    + getObjectiveDeck() List ~ ObjectiveCard ~
}

class GameModel{
    - gameResources : GameResources
    - board : Map ~ PlayerModel, Integer ~

    - playersList : List ~ Integer ~
    - currPlayerID : int
    - firstPlayerID : int

    - resourceCardsDeck : UsableCardsDeck
    - goldenCardsDeck : UsableCardsDeck
    - starterCardsDeck : StarterCardsDeck
    - ObjectiveCardsDeck : ObjectiveCardsDeck

    + getPlayerIDList() List ~ Integer ~
    + getCurrPlayerID() int
    + getFirstPlayerID() int

    + initializeDecks() void
    + shuffleAllDecks() void
    + setVisibleCards() void
    + drawResourceCard() PlaceableCard
    + drawGoldenCard() PlaceableCard
    + drawStarterCard() PlaceableCard
    + drawObjectiveCard() ObjectiveCard

    + selectToken(token)
    + endTurn() void
}

GameModel *-->"2" UsableCardsDeck
GameModel *-->"1" ObjectiveCardsDeck
GameModel *-->"1" StarterCardsDeck

class UsableCardsDeck{
    - deck : List ~ PlaceableCard ~
    - visibleCards : List ~ PlaceableCard ~

    + shuffleDeck() void
    + drawCard() PlaceableCard
    + drawVisibleCard(index : int) PlaceableCard
    + restoreVisibleCards() void
}

class ObjectiveCardsDeck{
    - deck : List ~ ObjectiveCard ~
    - commonObjectives : List ~ ObjectiveCard ~

    + getCommonObjectives() List ~ ObjectiveCard ~

    + shuffleDeck() void
    + drawCard() ObjectiveCard
    + setCommonObjectives() void
}

class StarterCardsDeck{
    - deck : List ~ StarterCard ~

    + shuffleDeck() void
    + drawCard() StarterCard
}

%% class GameView{
%%     + update() void
%% }

class GameController{
    - CardIDMap : Map ~ Integer, Card ~
    - PlayerIDMap : Map ~ Integer, PlayerModel ~

    + initializeGame() void
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

Post review UML

```mermaid

classDiagram

class MessageEncoder{
    <<abstract>>
    + sendId(int id) void
    + setState(PlayerState state) void
    + setCurrScore(int score) void
    + setToken(token token) void
    + setObjectiveToChoose(List<ObjectiveCard> objectives) void
    + setFirstPlayerToken(Token token) void
    + addCardToPlayerHand(PlaceableCard card) void
    + setSecretObjective(ObjectiveCard card) void
    + setStarterCard(PlaceableCard card) void
    + placeStarterCard(boolean placed) void
    + placeCard(boolean placed) void
}

class RMIEncoder{
    - client : ClientRMI
}

class SocketEncoder{
    - out : PrintWriter
}

MessageEncoder <|-- RMIEncoder
MessageEncoder <|-- SocketEncoder

PlayerModel*--> MessageEncoder


class PlaceableCard{
    - cardType : String
    - id : int
    - elementType : CardElementType
    - priority : int
    - currSide : Side
    - front : Side
    - back : Side

    + getResources() List ~ ResourceType ~
    + setPriority(int newPriority) void
    + getElementType() CardElementType
    + getBack() Side
    + getFront() Side
    + getCurrSide() Side
    + getId() int
    + flipCard() void
    + hasEnoughRequiredResources(int[] numOfResourcesArr) boolean
    + calculatePlacementPoints(int numOfCoveredCorners, int[] numOfResourcesArr) int
}

class ResourceCard{
    - points : int

    + getPoints() int
}

class GoldenCard{
    - pointsPerResource : int
    - pointsPerCoveredCorner : int
    - isPointPerResource : boolean
    - pointPerResourceRes : ResourceType
    - requiredResources : HashMap ~ ResourceType, Integer ~
    - points : int
    
    + getPoints() int
}

class ObjectiveCard{
    <<abstract>>
    - id : int
    - points : int 
    - objectiveType : String

    + getId() int
    + getPoints() int
    + calculateObjectivePoints(HashMap<PlaceableCard, Position> playArea, int[] numOfResourcesArr) int
}

class ResourcesCountObjectiveCard{
    - points : int
    - resourceType : ResourceType
    - requiredResourceCount : int
}

class TrinityObjectiveCard{
    - points : int
}

class DiagonalObjectiveCard{
    <<abstract>>
    - points : int
    - elementType: CardElementType

    # calculateObjectivePoints(HashMap~Position, PlaceableCard~ playArea, int direction) int
}

class TLBRDiagonalObjectiveCard{
    - direction : int
}

class TRBLDiagonalObjectiveCard{
    - direction : int
}

class LShapeObjectiveCard{
    <<abstract>>
    - points : int
    - mainElementType: CardElementType
    - secondaryElementType: CardElementType
    
    # calculateObjectivePoints(HashMap~Position, PlaceableCard~ playArea, int xDirection, int yDirection) int
}

class TLLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

class TRLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

class BLLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

class BRLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

ObjectiveCard <|-- DiagonalObjectiveCard
ObjectiveCard <|-- LShapeObjectiveCard
ObjectiveCard <|-- ResourcesCountObjectiveCard
ObjectiveCard <|-- TrinityObjectiveCard

DiagonalObjectiveCard <|-- TLBRDiagonalObjectiveCard
DiagonalObjectiveCard <|-- TRBLDiagonalObjectiveCard

LShapeObjectiveCard <|-- TLLShapeObjectiveCard
LShapeObjectiveCard <|-- TRLShapeObjectiveCard
LShapeObjectiveCard <|-- BLLShapeObjectiveCard
LShapeObjectiveCard <|-- BRLShapeObjectiveCard

PlaceableCard <|-- ResourceCard
PlaceableCard <|-- GoldenCard
PlaceableCard <|-- StarterCard
PlaceableCard *-->"3" Side

ResourceCard "cardType"*-->"1" CardElementType
GoldenCard "cardType"*-->"1" CardElementType

class Side{
    <<abstract>>
    - sideType : String
    - BLCorner : CardCorner
    - BRCorner : CardCorner
    - TLCorner : CardCorner
    - TRCorner : CardCorner

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
    - cornerType : String

    + isAvailable() boolean
    + getResource() ResourceType
}

class ResourceCorner{
    - resourceType : ResourceType    
}

class VisibleCorner{
    <<abstract>>
}

CardCorner <|-- VisibleCorner
CardCorner <|-- HiddenCorner
VisibleCorner <|-- ResourceCorner
VisibleCorner <|-- EmptyCorner

class Position{
    - xPos : int
    - yPos : int

    + getX() int
    + getY() int
}

class CardElementType{
    <<enumeration>>
    STARTER
    ANIMAL
    PLANT
    FUNGI
    INSECT
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
    + resourceTypeFromString(String str) ResourceType
}

class PlayerModel{
    - nickname : String
    - id : int
    - hand : List ~ PlaceableCard ~

    - objectiveToChoose : List ~ ObjectiveCard ~
    - secretObjective : ObjectiveCard
    - starterCard : PlaceableCard


    - playArea : Map ~ Position, PlaceableCard ~
    - numOfResourcesArr : int[]
    - currScore : int
    - numOfCompletedObjectives : int

    - state : PlayerState
    - currMaxPriority : int

    - token : Token
    - firstPlayerToken : Token

    - messageDecoder : MessageDecoder

    + getID() int
    + getNickname() String
    + getHand() List ~ PlaceableCard ~
    + setObjectivesToChoose(List ~ ObjectiveCard ~ objectives) void
    + getSectretObjective() ObjectiveCard
    + setSecretObjective(int index) void
    + getStarterCard () PlaceableCard
    + setStarterCard(PlaceableCard sCard) void
    + getPlayArea() Map ~ Position, PlaceableCard ~
    + getNumOfResourcesArr() int[]
    + getCurrScore() int
    + setCurrScore(int score) void
    + getNumOfCompletedObjectives() int
    + setToken(Token token) void
    + isFirstPlayer() boolean
    + setAsFirstPlayer(boolean isFirstPlayer) void
    + getState() PlayerState
    + setState(PlayerState state) void
    + getCurrMaxPriority() int
    + placeStarterCard() void
    + placeCard(PlaceableCard card, Position pos) void
    + getAdjacentCorners(Position pos) List ~ CardCorner ~
    + calculateObjectivePoints(ObjectiveCard oCard) void
    + flipStarterCard() void
    + drawCard(PlaceableCard card) void
    
}
class PlayerState{
    <<enumeration>>
    PRE_GAME
    PLACING_STARTER
    CHOOSING_TOKEN
    CHOOSING_OBJECTIVE
    WAITING
    PLACING
    DRAWING
}

%% class PlayerView{
%%     <<Mettere>>
%%     + setStarterCardChoice() void
%%     + flipStarterCard() void
%%     + placeStarterCard() void
%%     + placeCard(card : Card) void
%% }

class GameResources{
    - resourceCardsFilename : String
    - goldenCardsFilename : String
    - starterCardsFilename : String
    - objectiveCardsFilename : String

    - goldenDeck : List ~ PlaceableCard ~
    - resourcesDeck : List ~ PlaceableCard ~
    - starterDeck : List ~ PlaceableCard ~
    - objectiveDeck : List ~ ObjectiveCard ~


    + deserializeCorner() CardCorner
    + deserializeFront() Side
    + deserializeBack() Side
    + deserializeResourceCard() PlaceableCard
    + deserializeGoldenCard() PlaceableCard
    + deserializeStarterCard() PlaceableCard
    + deserializeObjectiveCard() ObjectiveCard
    + initializeResourceDeck() void
    + initializeGoldenDeck() void
    + initializeStarterDeck() void
    + initializeObjectiveDeck() void
    + initializeAllDecks() void
    
    + getResourcesDeck() List ~ PlaceableCard ~
    + getGoldenDeck() List ~ PlaceableCard ~
    + getStarterDeck() List ~ PlaceableCard ~
    + getObjectiveDeck() List ~ ObjectiveCard ~
}

class MatchModel{
    - maxPlayers : int
    - currPlayers : int
    - matchID : int

    - IDToPlayerMap : Map ~ Integer, PlayerModel ~

    - resourceCardsDeck : UsableCardsDeck
    - goldenCardsDeck : UsableCardsDeck
    - objectiveCardsDeck : ObjectiveCardsDeck
    - starterCardsDeck : StarterCardsDeck

    - currPlayerID : int
    - firstPlayerID : int
    - orderOfPlayersIDs : int[]

    - gameState : GameState

    + getIDToPlayerMap() Map ~ Integer, PlayerModel ~
    + getMaxPlayers() int
    + getCurrPlayers() int
    + getMatchID() int
    + getResourceCardsDeck() UsableCardsDeck
    + getGoldenCardsDeck() UsableCardsDeck
    + getObjectiveCardsDeck() ObjectiveCardsDeck
    + getStarterCardsDeck() StarterCardsDeck
    + getCurrPlayerID() int
    + getGameState() GameState
    + setGameState(GameState gameState) void
    + addPlayer(PlayerModel player) void

    + initializeDecks() void
    + shuffleAllDecks() void
    + setVisibleCards() void
    + drawResourceCard() PlaceableCard
    + drawGoldenCard() PlaceableCard
    + drawStarterCard() PlaceableCard
    + drawObjectiveCard() ObjectiveCard
    + drawVisibleResourceCard(int index) PlaceableCard
    + drawVisibleGoldenCard(int index) PlaceableCard
    + restoreVisibleResourceCard() void
    + restoreVisibleGoldenCard() void
    + setRandomFirstPlayer() void
    + calculateOrderOfPlayers() void
    + endTurn() void
    + checkForEndGame() void
    + manageEndGame() void
    + determineRanking() void
}

MatchModel *-->"2" UsableCardsDeck
MatchModel *-->"1" ObjectiveCardsDeck
MatchModel *-->"1" StarterCardsDeck

class UsableCardsDeck{
    - deck : List ~ PlaceableCard ~
    - visibleCards : List ~ PlaceableCard ~

    + getDeck() List ~ PlaceableCard ~
    + getVisibleCards() List ~ PlaceableCard ~
    + shuffleDeck() void
    + restoreInitialVisibleCards() void
    + drawCard() PlaceableCard
    + drawVisibleCard(int index) PlaceableCard
    + restoreVisibleCards() void
    + restoreVisibleCardWithOtherDeck(PlaceableCard card) void
    + isDeckEmpty() boolean

}

class ObjectiveCardsDeck{
    - deck : List ~ ObjectiveCard ~
    - commonObjectives : List ~ ObjectiveCard ~

    + getDeck() List ~ ObjectiveCard ~
    + getCommonObjectives() List ~ ObjectiveCard ~

    + shuffleDeck() void
    + drawCard() ObjectiveCard
    + setCommonObjectives() void
}

class StarterCardsDeck{
    - deck : List ~ StarterCard ~

    + getDeck() List ~ PlaceableCard ~
    + shuffleDeck() void
    + drawCard() PlaceableCard
}

class MatchController{
    - IDPlaceableCardMap : Map ~ Integer, PlaceableCard ~
    - IDObjectiveCardMap : : Map ~ Integer, ObjectiveCard ~
    - IDPlayerMap : Map ~ Integer, PlayerModel ~
    - matchModel : MatchModel

    + getIDPlaceableCardMap() Map ~ Integer, PlaceableCard ~
    + getIDPlayerMap() Map ~ Integer, PlayerModel ~
    + getMatchModel() MatchModel
    + addPlayer(String nickname) Integer
    + setUpGame() void
    + initializeDecks() void
    + shuffleAllDecks() void
    + setVisibleCards() void
    + setCommonObjectives() void
    + setObjectivesToChoose(int playerID) void
    + setRandomFirstPlayer() void
    + calculateOrderOfPlayers() void
    + placeCard(int playerID, int cardID, Position pos) void
    + fillPlayerHand(int playerID) void
    + drawResourceCard(int platerId) void
    + drawVisibleResourceCard(int playerID, int index) void
    + drawGoldenCard(int playerID) void
    + drawVisibleGoldenCard(int playerID, int index) void
    + drawStarterCard(int playerID) void
    + flipCard(int playerID, int cardID) void
    + endGameSetUp() void
    + endTurn() void
    + checkPlayerDrawExceptions(int playerID) void
}

MatchController *-->"1" MatchModel
MatchController *-->"1 ... n" PlayerModel

class Token{
    - color : TokenColors

    + getColor() TokenColors
}
class TokenColors{
    <<enumeration>>
    RED
    BLUE
    GREEN
    YELLOW
    BLACK

    - value :int

    + getValue() int
    + getColorFromInt(int n) TokenColors
}

class GameModel{
    - matches : List ~ MatchController ~

    + getMatch(int matchID) MatchController
    + createGame(String nickname, int numberOfPlayers) void
    + selectGame(int matchId) void
    + joinGame(int matchID, String nickname) void
    + startGame(int matchID) void
}

class GameController{
    - gameModel : GameModel

    + createGame(String nickname, int numberOfPlayers) void
    + selectGame(int matchID) void
    + joinGame(int matchID, String nickname) void
    + startGame(int matchID) void
}

GameModel *-->"1...n" MatchController
GameController *-->"1" GameModel

```

UML 2 Peer Review

```mermaid

classDiagram

class PlaceableCard{
    <<Abstract>>
    - cardType : String
    - id : int
    - elementType : CardElementType
    - priority : int
    - chosenSide : Side
    - front : Side
    - back : Side

    + getId() int
    + getElementType() CardElementType
    + setPriority(int newPriority) void
    + getChosenSide() Side
    + setChosenSide(Side chosenSide) void
    + getFront() Side
    + getBack() Side
    + getResources(Side chosenSide) List ~ ResourceType ~
    + hasEnoughRequiredResources(int[] numOfResourcesArr) abstract boolean
    + calculatePlacementPoints(int numOfCoveredCorners, int[] numOfResourcesArr) abstract int
}

class ResourceCard{
    - points : int

    + getPoints() int
}

class GoldenCard{
    - pointsPerResource : int
    - pointsPerCoveredCorner : int
    - isPointPerResource : boolean
    - pointPerResourceRes : ResourceType
    - requiredResources : HashMap ~ ResourceType, Integer ~
    - points : int
    
    + getPoints() int
}

class ObjectiveCard{
    <<abstract>>
    - id : int
    - points : int 
    - objectiveType : String

    + getId() int
    + getPoints() int
    + calculateObjectivePoints(HashMap ~PlaceableCard, Position~ playArea, int[] numOfResourcesArr) int
}

class ResourcesCountObjectiveCard{
    - points : int
    - resourceType : ResourceType
    - requiredResourceCount : int
}

class TrinityObjectiveCard{
    - points : int
}

class DiagonalObjectiveCard{
    <<abstract>>
    - points : int
    - elementType: CardElementType

    # calculateObjectivePoints(HashMap~Position, PlaceableCard~ playArea, int direction) int
}

class TLBRDiagonalObjectiveCard{
    - direction : int
}

class TRBLDiagonalObjectiveCard{
    - direction : int
}

class LShapeObjectiveCard{
    <<abstract>>
    - points : int
    - mainElementType: CardElementType
    - secondaryElementType: CardElementType
    
    # calculateObjectivePoints(HashMap~Position, PlaceableCard~ playArea, int xDirection, int yDirection) int
}

class TLLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

class TRLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

class BLLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

class BRLShapeObjectiveCard{
    - xDirection : int
    - yDirection : int
}

ObjectiveCard <|-- DiagonalObjectiveCard
ObjectiveCard <|-- LShapeObjectiveCard
ObjectiveCard <|-- ResourcesCountObjectiveCard
ObjectiveCard <|-- TrinityObjectiveCard

DiagonalObjectiveCard <|-- TLBRDiagonalObjectiveCard
DiagonalObjectiveCard <|-- TRBLDiagonalObjectiveCard

LShapeObjectiveCard <|-- TLLShapeObjectiveCard
LShapeObjectiveCard <|-- TRLShapeObjectiveCard
LShapeObjectiveCard <|-- BLLShapeObjectiveCard
LShapeObjectiveCard <|-- BRLShapeObjectiveCard

PlaceableCard <|-- ResourceCard
PlaceableCard <|-- GoldenCard
PlaceableCard <|-- StarterCard
PlaceableCard *-->"3" Side

ResourceCard "cardType"*-->"1" CardElementType
GoldenCard "cardType"*-->"1" CardElementType

class Side{
    <<abstract>>
    - sideType : String
    - BLCorner : CardCorner
    - BRCorner : CardCorner
    - TLCorner : CardCorner
    - TRCorner : CardCorner

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
    - cornerType : String

    + isAvailable() boolean
    + getResource() ResourceType
}

class ResourceCorner{
    - resourceType : ResourceType    
}

class VisibleCorner{
    <<abstract>>
}

CardCorner <|-- VisibleCorner
CardCorner <|-- HiddenCorner
VisibleCorner <|-- ResourceCorner
VisibleCorner <|-- EmptyCorner

class Position{
    - xPos : int
    - yPos : int

    + getX() int
    + getY() int
}

class CardElementType{
    <<enumeration>>
    STARTER
    ANIMAL
    PLANT
    FUNGI
    INSECT
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
    + resourceTypeFromString(String str) ResourceType
}

class PlayerModel{
    - nickname : String
    - id : int
    - hand : List ~ PlaceableCard ~

    - objectiveToChoose : List ~ ObjectiveCard ~
    - secretObjective : ObjectiveCard
    - starterCard : PlaceableCard


    - playArea : HashMap ~ Position, PlaceableCard ~
    - numOfResourcesArr : int[]
    - currScore : int
    - numOfCompletedObjectives : int

    - token : Token
    - firstPlayerToken : Token

    - state : PlayerState
    - currMaxPriority : int

    - sender : Encoder

    + getID() int
    + getNickname() String
    + getHand() List ~ PlaceableCard ~

    + setObjectivesToChoose(List ~ ObjectiveCard ~ objectives) void
    + getSectretObjective() ObjectiveCard
    + setSecretObjective(int index) void
    + getStarterCard () PlaceableCard
    + setStarterCard(PlaceableCard sCard) void
    + getPlayArea() HashMap ~ Position, PlaceableCard ~
    + getNumOfResourcesArr() int[]
    + getCurrScore() int
    + setCurrScore(int score) void
    + getNumOfCompletedObjectives() int
    + setToken(Token token) void
    + isFirstPlayer() boolean
    + setAsFirstPlayer(boolean isFirstPlayer) void
    + getState() PlayerState
    + setState(PlayerState state) void
    + getCurrMaxPriority() int
    + placeStarterCard(Side chosenSide) void

    + placeCard(PlaceableCard card, Position pos, Side chosenSide) void
    + getAdjacentCorners(Position pos) List ~ CardCorner ~
    + calculateObjectivePoints(ObjectiveCard oCard) void
    + drawCard(PlaceableCard card) void
    + requestError() void
    
}

class PlayerState{
    <<enumeration>>
    PRE_GAME
    PLACING_STARTER
    CHOOSING_TOKEN
    CHOOSING_OBJECTIVE
    WAITING
    PLACING
    DRAWING
}

%% class PlayerView{
%%     <<Mettere>>
%%     + setStarterCardChoice() void
%%     + flipStarterCard() void
%%     + placeStarterCard() void
%%     + placeCard(card : Card) void
%% }

class GameResources{
    - resourceCardsFilename : String
    - goldenCardsFilename : String
    - starterCardsFilename : String
    - objectiveCardsFilename : String

    - goldenDeck : List ~ PlaceableCard ~
    - resourcesDeck : List ~ PlaceableCard ~
    - starterDeck : List ~ PlaceableCard ~
    - objectiveDeck : List ~ ObjectiveCard ~

    - IDToPlaceableCardMap : ~Integer, PlaceableCard~
    - IDToObjectiveCardMap : ~Integer, ObjectiveCard~


    + deserializeCorner() CardCorner
    + deserializeFront() Side
    + deserializeBack() Side
    + deserializeResourceCard() PlaceableCard
    + deserializeGoldenCard() PlaceableCard
    + deserializeStarterCard() PlaceableCard
    + deserializeObjectiveCard() ObjectiveCard
    + initializeResourceDeck() void
    + initializeGoldenDeck() void
    + initializeStarterDeck() void
    + initializeObjectiveDeck() void
    + fillCardMaps() void
    + initializeAllDecks() void

    + getPlaceableCardByID(int ID) PlaceableCard
    + getObjectiveCardByID(int ID) PlaceableCard

    + getResourcesDeck() List ~ PlaceableCard ~
    + getGoldenDeck() List ~ PlaceableCard ~
    + getStarterDeck() List ~ PlaceableCard ~
    + getObjectiveDeck() List ~ ObjectiveCard ~
    + getIDToPlaceableCardMap() Map ~Integer, PlaceableCard~
    + getIDToObjectiveCardMap() Map ~Integer, ObjectiveCard~
}

class MatchModel{
    - maxPlayers : int
    - currPlayers : int
    - matchID : int

    - IDToPlayerMap : HashMap ~ Integer, PlayerModel ~

    - resourceCardsDeck : UsableCardsDeck
    - goldenCardsDeck : UsableCardsDeck
    - objectiveCardsDeck : ObjectiveCardsDeck
    - starterCardsDeck : StarterCardsDeck

    - currPlayerID : int
    - firstPlayerID : int
    - orderOfPlayersIDs : int[]

    - gameState : GameState

    + getIDToPlayerMap() HashMap ~ Integer, PlayerModel ~
    + getNicknames() List ~ String ~
    + getNicknamesMap(int currPlayerID) HashMap ~ Integer, String ~
    + getMaxPlayers() int
    + getCurrPlayers() int
    + getMatchID() int
    + getResourceCardsDeck() UsableCardsDeck
    + getGoldenCardsDeck() UsableCardsDeck
    + getObjectiveCardsDeck() ObjectiveCardsDeck
    + getStarterCardsDeck() StarterCardsDeck
    + getCurrPlayerID() int
    + getGameState() GameState
    + setGameState(GameState gameState) void
    + addPlayer(PlayerModel player) void

    + initializeDecks() void
    + shuffleAllDecks() void
    + setVisibleCards() void
    + drawResourceCard() PlaceableCard
    + drawGoldenCard() PlaceableCard
    + drawStarterCard() PlaceableCard
    + drawObjectiveCard() ObjectiveCard
    + drawVisibleResourceCard(int index) PlaceableCard
    + drawVisibleGoldenCard(int index) PlaceableCard
    + restoreVisibleResourceCard() void
    + restoreVisibleGoldenCard() void
    + setRandomFirstPlayer() void
    + calculateOrderOfPlayers() void
    + endTurn() void
    + checkForEndGame() void
    + manageEndGame() void
    + determineRanking() void
}

MatchModel *-->"2" UsableCardsDeck
MatchModel *-->"1" ObjectiveCardsDeck
MatchModel *-->"1" StarterCardsDeck

class UsableCardsDeck{
    - deck : List ~ PlaceableCard ~
    - visibleCards : List ~ PlaceableCard ~

    + getDeck() List ~ PlaceableCard ~
    + getVisibleCards() List ~ PlaceableCard ~
    + shuffleDeck() void
    + restoreInitialVisibleCards() void
    + drawCard() PlaceableCard
    + drawVisibleCard(int index) PlaceableCard
    + restoreVisibleCards() void
    + restoreVisibleCardWithOtherDeck(PlaceableCard card) void
    + isDeckEmpty() boolean

}

class ObjectiveCardsDeck{
    - deck : List ~ ObjectiveCard ~
    - commonObjectives : List ~ ObjectiveCard ~

    + getDeck() List ~ ObjectiveCard ~
    + getCommonObjectives() List ~ ObjectiveCard ~

    + shuffleDeck() void
    + drawCard() ObjectiveCard
    + setCommonObjectives() void
}

class StarterCardsDeck{
    - deck : List ~ StarterCard ~

    + getDeck() List ~ PlaceableCard ~
    + shuffleDeck() void
    + drawCard() PlaceableCard
}

class MatchController{
    
    - IDPlayerMap : Map ~ Integer, PlayerModel ~
    - matchModel : MatchModel

    + getIDPlayerMap() Map ~ Integer, PlayerModel ~
    + getMatchModel() MatchModel

    + addPlayer(String nickname, PrintWriter out) Integer
    + addPlayer(String nickname, ClientRMIInterface clientRMI) Integer

    + setUpGame() void
    + initializeDecks() void
    + shuffleAllDecks() void
    + setVisibleCards() void
    + setCommonObjectives() void
    + setObjectivesToChoose(int playerID) void
    + setRandomFirstPlayer() void
    + calculateOrderOfPlayers() void
    + placeCard(int playerID, int cardID, Position pos, Side chosenSide) void
    + fillPlayerHand(int playerID) void
    + drawResourceCard(int platerId) void
    + drawVisibleResourceCard(int playerID, int index) void
    + drawGoldenCard(int playerID) void
    + drawVisibleGoldenCard(int playerID, int index) void
    + drawStarterCard(int playerID) void

    + endGameSetUp() void
    + endTurn() void
    - checkPlayerDrawExceptions(int playerID) void
    + requestError(int playerId) void

    %% nuovi metodi da implementare
    + selectedStarterSide(int playerID, PlaceableCard starterCard, Side side) void
    + selectSecretObjective(int playerID, int secretObjectiveCardID) void
    + drawCard(PlaceableCard placeableCardID) void

}

MatchController *-->"1" MatchModel
MatchController *-->"1 ... n" PlayerModel

class Token{
    - color : TokenColors

    + getColor() TokenColors
}
class TokenColors{
    <<enumeration>>
    RED
    BLUE
    GREEN
    YELLOW
    BLACK

    + getColorFromInt(int n) TokenColors
}

class GameModel{
    - matches : List ~ MatchController ~

    + getMatches() Map ~Integer, List ~String~ ~
    + getMatch(int matchID) MatchController
    + createGame(int numberOfPlayers) MatchController
    + selectGame(int matchId) void
    + startGame(MatchController matchController) void
}

class GameController{
    - gameModel : GameModel

    + getGameModel() GameModel


    + beginGameCreation() void


    + createGame(int numberOfPlayers) MatchController
    + selectGame(int matchID) MatchController
    + joinMatch(MatchController matchController, String nickname, PrintWriter out, BufferedReader in) Integer
    + joinMatch(MatchController matchController, String nickname, ClientRMIInterface client) Integer
    # analyzePlayerNumber(MatchController matchController)
    - sendMessage(String output, PrintWriter out) void
    - getMatchesSoket() String
    - getMatches() Map ~ Integer, List ~String~ ~
    - getMatchesNicknames(MatchController matchController) List ~String~
    + getMatches(PrintWriter out) void
    + getMatches(ClientRMIInterface client) void
    - sendMatch(int matchID) MatchController
    + sendMatch(int matchID, PrintWriter out) MatchController
    + sendMatch(int matchID, ClientRMIInterface client) MatchController
    + sendNickname(MatchController matchController, String nickname, PrintWriter out, BufferedReader in) void
    + sendNickname(MatchController matchController, String nickname, ClientRMIInterface client) void
    - createMatch(int numberOfPlayers) MatchController
    + createMatch(int numberOfPlayers, PrintWriter out) MatchController
    + createMatch(int numberOfPlayers, ClientRMIInterface client) MatchController
}

GameModel *-->"1...n" MatchController
GameController *-->"1" GameModel

class GameState{
    <<enumeration>>
    PREGAME
    SET_UP
    PLAYING
    FINAL_ROUND
    EXTRA_ROUND
    END_GAME
}

class Encoder{
    <<abstract>>
    + sendId(int id) void
    + setState(PlayerState state) void
    + setCurrScore(int score) void
    + setToken(token token) void
    # setTokenEncoded(TokenColors color) void
    + setObjectiveToChoose(List~ObjectiveCard~ objectives) void
    # setObjectivesToChooseEncoded(List~Integer~ objectiveIDs) void
    + setFirstPlayerToken(Token token) void
    # setFirstPlayerTokenEncoded(TokenColors color) void
    + addCardToPlayerHand(PlaceableCard card) void
    # addCardToPlayerHandEncoded(int id) void

    + setSecretObjective(ObjectiveCard card) void
    # setSecretObjectiveEncoded(int id) void
    + setStarterCard(PlaceableCard card) void
    # setStarterCardEncoded(int id) void
    + placeStarterCard(boolean placed) void
    + placeCard(boolean placed) void
    + requestError() void
}

class RMISender{
    - client : ClientRMIInterface
}

class SocketSender{
    - out : PrintWriter
    # SendMessage(String output) void
}

Encoder <|-- RMISender
Encoder <|-- SocketSender

PlayerModel*--> "1" Encoder

class Decoder {
    - matchController : MatchController
    - playerId : int

    + placeCard(int id, Position pos) void
    # placeCardDecoded(PlaceableCard card, Position pos) void
    + drawResourceCard() void
    + drawVisibleResourceCard(int playerID, int index) void
    + drawGoldenCard(int playerID) void
    + drawVisibleGoldenCard(int playerID, int index) void
    + requestError() void

    %% metodi nuovi da implementare
    + selectedStarterSide(in starterCardID, Side side) void
    # selectedStarterSideDecoded(PlaceableCard starterCard, Side side) void
    + selectSecretObjective(int secretObjectiveCardID) void
    # selectSecretObjectiveDecoded(PlaceableCard secretObjectiveCardID) void
    + drawCard(int placeableCardID) void
    # drawCard(PlaceableCard placeableCardID) void

}
RMIReceiver*--> "1...n"Decoder
SocketReceiver*--> "1" Decoder

class RMIReceiverInterface {
    <<Interface>>
    + getMatches(ClientRMIInterface client) void
    + sendMatch(int matchID, ClientRMIInterface client) void
    + sendNickname(String nickname, ClientRMIInterface client) void
    + createMatch(int numberOfPlayers, ClientRMIInterface client) void
    + placeCard(int id, Position pos, ClientRMIInterface client) void
    + drawResourceCard(ClientRMIInterface client) void
    + drawVisibleResourceCard(int playerID, int index, ClientRMIInterface client) void
    + drawGoldenCard(int playerID, ClientRMIInterface client) void
    + drawVisibleGoldenCard(int playerID, int index, ClientRMIInterface client) void
    + requestError(ClientRMIInterface client) void

    + createGame(ClientRMIInterface client, String nickname, int numberOfPlayers) void
    + selectMatch(ClientRMIInterface client, int matchID) MatchController
    + joinMatch(MatchController matchController, String nickname, ClientRMIInterface client) void

    %% metodi nuovi da implementare
    + selectedStarterSide(int starterCardID, Side side) void
    + selectSecretObjective(int secretObjectiveCardID) void
    + drawCard(int placeableCardID) void

}

RMIReceiverInterface <|-- RMIReceiver : implements

class RMIReceiver {
    - clientRMIDecoderHashMap : HashMap ~ClientRMIInterface, Decoder~
    - gameController : GameController
    - MatchControllerHashMap : HashMap~ClientRMIInterface, MatchController~

    + addDecoder(ClientRMIInterface client, Decoder decoder) void
    - addMatchController(ClientRMIInterface client, MatchController matchController) void
    - getMatchController(ClientRMIInterface client) MatchController

}

class SocketReceiver {
    - decoder : Decoder

    # messageDecoder(String input) void
}

class ClientRMIInterface {
    <<Interface>>
    + printMatches(Map~Integer, List~String~~ matchesNicknames) void
    + failedMatch(Map~Integer, List~String~~ matchesNicknames) void
    + joinMatch(int matchID, List~String~ nicknames) void
    + addNickname(Integer playerID, String nickname, Map~Integer, String~ opponents) void
    + failedNickname(List~String~ nicknames)
    + sendID(int id) void
    + setState(PlayerState state) void
    + setCurrScore(int score) void
    + setToken(TokenColors color) void
    + setObjectivesToChoose(List~Integer~ objectiveIDs) void
    + setFirstPlayerToken(TokenColors color) void
    + addCardToPlayerHand(int id) void
    + setSecretObjective(int id) void
    + setStarterCard(int id) void
    + placeStarterCard(boolean placed) void
    + placeCard(boolean placed) void
    + sendInitializedVisibleDecks(List ~ Integer ~ visibleResourceCardsIDs, List ~ Integer ~ visibleGoldenCardsIDs) void
    + sendStarterCard(int starterCardID) void
    + confirmStarterSide(int starterCardID, Side side) void
    + failureStarterSide() void
    + sendOpponentsStarterCard(int starterCardID, Side side, int playerID) void
    + sendToken(Token token, List~Token~ opponentsTokens) void
    + sendHand(List ~ Integer ~ handCardIDs) void
    + sendCommonObjectives(List ~ Integer ~commonObjectiveCardsIDs) void
    + sendSecretObjectives(List ~ Integer ~ secretObjectiveCardsIDs) void
    + confirmSecretObjective(int objectiveCardID) void
    + failureSecretObjective() void
    + firstPlayer(int playerID) void
    + confirmPlaceCard(int placeableCardID, Position pos) void
    + failurePlaceCard() void
    + confirmDrawCard(int placeableCardID, newVisibleCard) void
    + failureDrawCard() void
}

class RMIController {
    - gameController : GameController
    - startRMIReceiver(String[] args) void
}
class SocketController {
    + startServerConnection(String[] args) ServerSocket
    + connectClient(ServerSocket serverSocket) Socket
    + createClientConnectionOUT(Socket connectionSocket) PrintWriter
    + waitClientConnectionIN(Socket connectionSocket) BufferedReader
    + startInteraction(BufferedReader in, PrintWriter out, GameController gameController) void
    + messageDecoder(String input, GameController gameController, MatchController matchController, PrintWriter out, BufferedReader in) MatchController
}

```

Start socket connection sequence diagram

```mermaid

sequenceDiagram
    title Start socket connection sequence diagram

    participant C as ClientController
    participant Cn as Client Sender/Receiver
    participant Sn as SocketController
    participant G as GameController
    participant S as ServerMain

    Note right of S: Server: Server starts

    Note left of C: Client: Client starts

    C->>S: socket(hostName, portNumber)

    S->>Sn: startInteraction(in, out, gameController)

    Note left of C: Client: If there is a failure thow exception

    C->>Cn: getMatches()

    Cn-->>Sn: getMatches()

    Sn->>G: getMatches()

    G->>Sn: printMatches(matchesNicknames)
    Sn-->>Cn: printMatches(matchesNicknames)

    Cn->>C: printMatches(matchesNicknames)

    Note left of C: Client: Update view
    Note left of C: Client: Ask "new match or join"
    Note left of C: Client: Input validation

    alt Create game
        C->>Cn: createMatch(numbersOfPlayers)

        Cn-->>Sn: createMatch(numbersOfPlayers)

        Sn->>G: createMatch(numbersOfPlayers)

        Note right of G: Server: Create a new match

        G->>Sn: joinMatch(matchID, nicknames)
        Sn-->>Cn: joinMatch(matchID, nicknames)

        Cn->>C: joinMatch(matchID, nicknames)
    else Join game
        C->>Cn: sendMatch(matchID)

        Cn-->>Sn: sendMatch(matchID)

        Sn->>G: sendMatch(matchID)

        Note right of G: Server: Search match with matchID
        alt Success
            G->>Sn: joinMatch(matchID, nicknames)
            Sn-->>Cn: joinMatch(matchID, nicknames)

            Cn->>C: joinMatch(matchID, nicknames)
        else Failure
            G->>Sn: failedMatch(matchesNicknames)
            Sn-->>Cn: failedMatch(matchesNicknames)

            Cn->>C: failedMatch(matchesNicknames)
            Note left of C: Client Back to: Ask "new match or join"
        end
    end

    Note left of C: Client: Update view
    Note left of C: Client: Ask "nickname"

    C->>Cn: sendNickname(nickname)

    Cn-->>Sn: sendNickname(nickname)

    Sn->>G: sendNickname(nickname)

    Note right of G: Server: Check nicknames
    alt Success
        Note right of G: Server: Create PlayerModel,<br> SocketReceiver, Decoder,<br> Encoder = new SocketSender

        G->>Sn: addNickname(playerID, nickname, opponents)
        Sn-->>Cn: addNickname(playerID, nickname, opponents)

        Cn->>C: addNickname(playerID, nickname, opponents)
    else Failure
        G->>Sn: failedNickname(nicknames)
        Sn-->>Cn: failedNickname(nicknames)

        Cn->>C: failedNickname(nicknames)
        Note left of C: Client Back to: Ask "nickname"
    end
    
    Note right of G: Server: Wait for all players

```

Start RMI connection sequence diagram

```mermaid

sequenceDiagram
    title Start RMI connection sequence diagram

    participant C as ClientController
    participant Cn as Client Sender/Receiver
    participant Sn as RMIreceiver
    participant G as GameController
    participant S as ServerMain

    Note right of S: Server: Server starts

    S->>Sn: startRMIreceiver()

    Note left of C: Client: Client starts

    C->>S: startRMI(hostName, port)

    Note left of C: Client: If there is a failure thow exception

    C->>Cn: getMatches(client)

    Cn->>Sn: getMatches(client)

    Sn->>G: getMatches(client)

    G->>Sn: printMatches(matchesNicknames)
    Sn->>Cn: printMatches(matchesNicknames)

    Cn->>C: printMatches(matchesNicknames)

    Note left of C: Client: Update view
    Note left of C: Client: Ask "new match or join"
    Note left of C: Client: Input validation

    alt Create game
        C->>Cn: createMatch(numbersOfPlayers, client)

        Cn->>Sn: createMatch(numbersOfPlayers, client)

        Sn->>G: createMatch(numbersOfPlayers, client)

        Note right of G: Server: Create a new match

        G->>Sn: joinMatch(matchID, nicknames)
        Sn->>Cn: joinMatch(matchID, nicknames)

        Cn->>C: joinMatch(matchID, nicknames)
    else Join game
        C->>Cn: sendMatch(matchID, client)

        Cn->>Sn: sendMatch(matchID, client)

        Sn->>G: sendMatch(matchID, client)

        Note right of G: Server: Search match with matchID
        alt Success
            G->>Sn: joinMatch(matchID, nicknames)
            Sn->>Cn: joinMatch(matchID, nicknames)

            Cn->>C: joinMatch(matchID, nicknames)
        else Failure
            G->>Sn: failedMatch(matchesNicknames)
            Sn->>Cn: failedMatch(matchesNicknames)

            Cn->>C: failedMatch(matchesNicknames)
            Note left of C: Client Back to: Ask "new match or join"
        end
    end

    Note left of C: Client: Update view
    Note left of C: Client: Ask "nickname"

    C->>Cn: sendNickname(nickname, client)

    Cn->>Sn: sendNickname(nickname, client)

    Sn->>G: sendNickname(nickname, client)

    Note right of G: Server: Check nicknames
    alt Success
        Note right of G: Server: Create PlayerModel, Decoder,<br>RMIreceiver.addDecoder(client, decoder),<br> Encoder = new RMISender

        G->>Sn: addNickname(playerID, nickname, opponents)
        Sn->>Cn: addNickname(playerID, nickname, opponents)

        Cn->>C: addNickname(playerID, nickname, opponents)
    else Failure
        G->>Sn: failedNickname(nicknames)
        Sn->>Cn: failedNickname(nicknames)

        Cn->>C: failedNickname(nicknames)
        Note left of C: Client Back to: Ask "nickname"
    end
    
    Note right of G: Server: Wait for all players

```

Sequence diagram for game setup

```mermaid

sequenceDiagram
    title Game setup sequence diagram

    participant C as ClientController
    participant Cn as Client Sender/Receiver
    participant Sn as Server Decoder/Encoder
    participant M as MatchController
    
    Note right of M: Server: All players connected
    Note right of M: Server: Game start
    Note right of M: Server: Initialize decks

    M->>Sn: sendInitializedVisibleDecks(visibleResourceCards,<br> visibleGoldenCards)

    Sn-->>Cn: sendInitializedVisibleDecks(visibleResourceCardsIDs,<br> visibleGoldenCardsIDs)

    Cn->>C: sendInitializedVisibleDecks(visibleResourceCards,<br> visibleGoldenCards)

    M->>Sn: sendStarterCard(starterCard)

    Sn-->>Cn: sendStarterCard(starterCardID)

    Cn->>C: sendStarterCard(starterCard)

    Note left of C: Client: Update view
    Note left of C: Client: Ask "Starter card side"
    Note left of C: Client: Input validation

    C->>Cn: selectedStarterSide(starterCard, side)

    Cn-->>Sn: selectedStarterSide(starterCardID, side)

    Sn->>M: selectedStarterSide(starterCard, side)

    Note right of M: Server: Starter card validation

    alt Success
        Note right of M: Server: Store starter card
        M->>Sn: confirmStarterSide(starterCard, side)

        Sn-->>Cn: confirmStarterSide(starterCardID, side)

        Cn->>C: confirmStarterSide(starterCard, side)
        Note left of C: Client: Update view
    else Failure
        M->>Sn: failureStarterSide()

        Sn-->>Cn: failureStarterSide()

        Cn->>C: failureStarterSide()
        Note left of C: Client back to: Ask "Starter card side"
    end

    Note right of M: Server: Wait for all players

    M->>Sn: sendOpponentsStarterCard(starterCard,<br> side, playerID)

    Sn-->>Cn: sendOpponentsStarterCard(starterCardID,<br> side, playerID)

    Cn->>C: sendOpponentsStarterCard(starterCard,<br> side, playerID)

    M->>Sn: sendToken(Token, opponentsTokens)

    Sn-->>Cn: sendToken(Token, opponentsTokens)

    Cn->>C: sendToken(Token, opponentsTokens)

    M->>Sn: sendHand(handCards)

    Sn-->>Cn: sendHand(handCardIDs)

    Cn->>C: sendHand(handCards)

    M->>Sn: sendCommonObjectives(commonObjectiveCards)

    Sn-->>Cn: sendCommonObjectives(commonObjectiveCardsIDs)

    Cn->>C: sendCommonObjectives(commonObjectiveCards)

    M->>Sn: sendSecretObjectives(secretObjectiveCards)

    Sn-->>Cn: sendSecretObjectives(secretObjectiveCardsIDs)

    Cn->>C: sendSecretObjectives(secretObjectiveCards)

    Note left of C: Client: Update view
    Note left of C: Client: Ask "Secret objective"
    Note left of C: Client: Input validation

    C->>Cn: selectSecretObjective(secretObjectiveCard)

    Cn-->>Sn: selectSecretObjective(secretObjectiveCardID)

    Sn->>M: selectSecretObjective(secretObjectiveCard)

    Note right of M: Server: Secret objective validation

    alt Success
        Note right of M: Server: Store secret objective
        M->>Sn: confirmSecretObjective(objectiveCard)

        Sn-->>Cn: confirmSecretObjective(objectiveCardID)

        Cn->>C: confirmSecretObjective(objectiveCard)
        Note left of C: Client: Update view
    else Failure
        M->>Sn: failureSecretObjective()

        Sn-->>Cn: failureSecretObjective()

        Cn->>C: failureSecretObjective()
        Note left of C: Client back to: Ask "Secret objective"
    end

    Note right of M: Server: Wait for all players

    Note right of M: Server: Choose random first player

    M->>Sn: firstPlayer(playerID)

    Sn-->>Cn: firstPlayer(playerID)

    Cn->>C: firstPlayer(playerID)

    Note left of C: Client: Update view

    Note right of M: Server: Start game

```

Your turn sequence diagram

```mermaid

sequenceDiagram
    title Your turn sequence diagram

    participant C as ClientController
    participant Cn as Client Sender/Receiver
    participant Sn as Server Decoder/Encoder
    participant M as MatchController
    
    Note left of C: Client:<br><br> If you are the first player<br>Client: Ask "Place card"<br>else wait your turn
    Note left of C: Client: Ask "Place card"
    Note left of C: Client: Place card validation

    C->>Cn: placeCard(placeableCard, pos)

    Cn-->>Sn: placeCard(placeableCardID, pos)

    Sn->>M: placeCard(placeableCard, pos)

    Note right of M: Server: Place card validation

    alt Success
        Note right of M: Server: Store placed card
        M->>Sn: confirmPlaceCard(placeableCard, pos)

        Sn-->>Cn: confirmPlaceCard(placeableCardID, pos)

        Cn->>C: confirmPlaceCard(placeableCard, pos)
        Note left of C: Client: Update view
    else Failure
        M->>Sn: failurePlaceCard()

        Sn-->>Cn: failurePlaceCard()

        Cn->>C: failurePlaceCard()
        Note left of C: Client back to: Ask "Place card"
    end

    Note right of M: Server: Notify opponents

    Note left of C: Client: Ask "Draw card"
    Note left of C: Client: Draw card validation

    C->>Cn: drawCard(placeableCard)

    Cn-->>Sn: drawCard(placeableCardID)

    Sn->>M: drawCard(placeableCard)

    Note right of M: Server: Draw card validation

    alt Success
        Note right of M: Server: Store drawn card
        M->>Sn: confirmDrawCard(placeableCard, newVisibleCard)

        Sn-->>Cn: confirmDrawCard(placeableCardID, newVisibleCard)

        Cn->>C: confirmDrawCard(placeableCard, newVisibleCard)
        Note left of C: Client: Update view
    else Failure
        M->>Sn: failureDrawCard()

        Sn-->>Cn: failureDrawCard()

        Cn->>C: failureDrawCard()
        Note left of C: Client back to: Ask "Draw card"
    end

    Note right of M: Server: Notify opponents
    Note right of M: Server: checkForEndGame()<br>If true "check notes"
    Note right of M: Server: Pick next player
    Note right of M: Server: Notify all players<br> next player ID


```

End game sequence diagram

```mermaid
sequenceDiagram
    title Extra round turn sequence diagram

    participant C as ClientController
    participant Cn as Client Sender/Receiver
    participant Sn as Server Decoder/Encoder
    participant M as MatchController
    
    Note left of C: Client:<br><br> If nextPlayerID is your ID<br>Client: Ask "Place card"<br>else wait your turn
    Note left of C: Client: Ask "Place card"
    Note left of C: Client: Place card validation

    C->>Cn: placeCard(placeableCard, pos)

    Cn-->>Sn: placeCard(placeableCardID, pos)

    Sn->>M: placeCard(placeableCard, pos)

    Note right of M: Server: Place card validation

    alt Success
        Note right of M: Server: Store placed card
        M->>Sn: confirmPlaceCard(placeableCard, pos)

        Sn-->>Cn: confirmPlaceCard(placeableCardID, pos)

        Cn->>C: confirmPlaceCard(placeableCard, pos)
        Note left of C: Client: Update view
    else Failure
        M->>Sn: failurePlaceCard()

        Sn-->>Cn: failurePlaceCard()

        Cn->>C: failurePlaceCard()
        Note left of C: Client back to: Ask "Place card"
    end

    Note right of M: Server: Notify opponents
    Note right of M: Server: Pick next player
    Note right of M: Server: Notify all players<br> next player ID<br>or End Game state
    Note right of M: Server: If End Game<br>matchController<br>determines ranking

    Note right of M: Server: Notify all
    M->>Sn: finalRanking(ranking)

    Sn-->>Cn: finalRanking(ranking)

    Cn->>C: finalRanking(ranking)
    Note left of C: Client: Update view

```

New Start socket connection sequence diagram

```mermaid
sequenceDiagram
    title Start socket connection sequence diagram

    participant C as ClientMain
    participant Cn as Client Receiver / Client Sender
    participant D as Decoder / Sender
    participant G as GameController
    participant M as MatchController
    participant S as ServerMain

    Note right of S: Server: Server starts and creates Game Controller
    
    S->>D : Decoder.setGameController(gameController) 

    Note left of C: Client: Client
    C->>S  : socket(hostName, portNumber)
    S->>D : executor.submit(SocketReceiver(socket, GameController))

    Note right of G: Server: Creates PlayerID

    Note left of C: Client: If there is a failure thow exception

    C->>Cn: getMatches()

    Cn-->>D: getMatches()
    D->>G: getMatches()
    
    G->>D : sendMatches(matches, playerID)
    D-->>Cn : sendMatches(matches, playerID)

    Note left of Cn: Client: Update view
    Note left of Cn: Client: Ask "new match or join"
    Note left of Cn: Client: Input validation

    alt Create game

        Cn-->>D: createMatch(numbersOfPlayers, nickname)

        D->>G: createMatch(playerID, numbersOfPlayers, nickname)

        Note right of G: Server: Creates a new match with hostNickname

        alt Success
        G->>D: createMatchResult(matchID, hostNickname)
        D-->>Cn: createMatchResult(matchID, hostNickname)
        else Failure
        G->>D: sendError(errorMsg)
        D-->>Cn: sendError(String errorMsg)
    
    end


    else SelectMatch

        Cn-->>D: selectMatch(matchID)
        D->>G : selectMatch(matchID)
        Note right of G: Server: Searches match with matchID and select MatchController

        alt Success
        G->>D: selectMatchResult(matchID, nicknames)
        D->>Cn: selectMatchResult(matchID, nicknames)
        else Failure
        G->>D: sendError(errorMsg)
        D-->>Cn: sendError(String errorMsg)
    end

    end

    Note left of Cn: Client: Update view
    Note left of Cn: Client: Ask "nickname"

    Cn-->>D: chooseNickname(nickname)

    D->>M: chooseNickname(nickname)
    Note right of M: Server: Check nicknames
    
    
    alt Success
        
        M->>D: chooseNickname(nickname)
        D-->>Cn : chooseNickname(nickname)
    else Failure
        M->>D: sendError(errorMsg)
        D-->>Cn: sendError(String errorMsg)
        Note left of Cn: Client Back to: Ask "nickname"
    end
    
    Note right of M: Server: Wait for all players
```

New Start RMI connection sequence diagram

```mermaid
sequenceDiagram
    title Start RMI connection sequence diagram

    participant C as ClientMain
    participant Cn as Client Receiver / Client Sender
    participant D as Receiver/ Sender
    participant G as GameController
    participant M as MatchController
    participant S as ServerMain

    Note right of S: Server: Server starts and creates Game Controller
    
    S->>D : new RMIServer(gameController)

    Note left of C: Client: Client starts 


    Note left of C: Client: If there is a failure thow exception

    C->>Cn: getMatches(client)

    Cn-->>D: getMatches(client)

    D->>G: getMatches()
    Note right of G: Server: Creates PlayerID
    G->>D : sendMatches(matches, playerID) 
    D-->>Cn : sendMatches(matches, playerID)

    Note left of Cn: Client: Update view
    Note left of Cn: Client: Ask "new match or join"
    Note left of Cn: Client: Input validation

    alt Create game

        Cn-->>D: createMatch(numberOfPlayers, nickname)

        D->>G: createMatch(playerID, numbersOfPlayers, nickname)

        Note right of G: Server: Creates a new match with hostNickname

        alt Success
        G->>D: createMatchResult(matchID, hostNickname)
        D-->>Cn: createMatchResult(matchID, hostNickname)
        else Failure
        G->>D: sendError(errorMsg)
        D-->>Cn: sendError(String errorMsg)
    
    end


    else SelectMatch

        Cn-->>D: selectMatch(matchID)
        D->>G : selectMatch(matchID)
        Note right of G: Server: Searches match with matchID and select MatchController

        alt Success
        G->>D: selectMatchResult(matchID, nicknames)
        D->>Cn: selectMatchResult(matchID, nicknames)
        else Failure
        G->>D: sendError(errorMsg)
        D-->>Cn: sendError(String errorMsg)
    end

    end

    Note left of Cn: Client: Update view
    Note left of Cn: Client: Ask "nickname"

    Cn-->>D: chooseNickname(nickname)

    D->>M: chooseNickname(nickname)
    Note right of M: Server: Check nicknames
    
    
    alt Success
        
        M->>D: chooseNicknameResult(nickname)
        D-->>Cn : chooseNicknameResult(nickname)
    else Failure
        M->>D: sendError(errorMsg)
        D-->>Cn: sendError(String errorMsg)
        Note left of Cn: Client Back to: Ask "nickname"
    end
    
    Note right of M: Server: Wait for all players
```

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
Start socket connection sequence diagram

```mermaid

sequenceDiagram
    title Start socket connection sequence diagram

    participant C as ClientController
    participant Cn as Client Decoder/Encoder
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

    G-->>Cn: printMatches(matchesNicknames)

    Cn->>C: printMatches(matchesNicknames)

    Note left of C: Client: Update view
    Note left of C: Client: Ask "new match or join"
    Note left of C: Client: Input validation

    alt Create game
        C->>Cn: createMatch(numbersOfPlayers)

        Cn-->>Sn: createMatch(numbersOfPlayers)

        Sn->>G: createMatch(numbersOfPlayers)

        Note right of G: Server: Create a new match

        G-->>Cn: joinMatch(matchID, nicknames)

        Cn->>C: joinMatch(matchID, nicknames)
    else Join game
        C->>Cn: sendMatch(matchID)

        Cn-->>Sn: sendMatch(matchID)

        Sn->>G: sendMatch(matchID)

        Note right of G: Server: Search match with matchID
        alt Success
            G-->>Cn: joinMatch(matchID, nicknames)

            Cn->>C: joinMatch(matchID, nicknames)
        else Failure
            G-->>Cn: failedMatch(matchesNicknames)

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

        G-->>Cn: addNickname(playerID, nickname, opponents)

        Cn->>C: addNickname(playerID, nickname, opponents)
    else Failure
        G-->>Cn: failedNickname(nicknames)

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
    participant Cn as Client Decoder/Encoder
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

    G->>Cn: printMatches(matchesNicknames)

    Cn->>C: printMatches(matchesNicknames)

    Note left of C: Client: Update view
    Note left of C: Client: Ask "new match or join"
    Note left of C: Client: Input validation

    alt Create game
        C->>Cn: createMatch(numbersOfPlayers, client)

        Cn->>Sn: createMatch(numbersOfPlayers, client)

        Sn->>G: createMatch(numbersOfPlayers, client)

        Note right of G: Server: Create a new match

        G->>Cn: joinMatch(matchID, nicknames)

        Cn->>C: joinMatch(matchID, nicknames)
    else Join game
        C->>Cn: sendMatch(matchID, client)

        Cn->>Sn: sendMatch(matchID, client)

        Sn->>G: sendMatch(matchID, client)

        Note right of G: Server: Search match with matchID
        alt Success
            G->>Cn: joinMatch(matchID, nicknames)

            Cn->>C: joinMatch(matchID, nicknames)
        else Failure
            G->>Cn: failedMatch(matchesNicknames)

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

        G->>Cn: addNickname(playerID, nickname, opponents)

        Cn->>C: addNickname(playerID, nickname, opponents)
    else Failure
        G->>Cn: failedNickname(nicknames)

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
    participant Cn as Client Decoder/Encoder
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
    participant Cn as Client Decoder/Encoder
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
    participant Cn as Client Decoder/Encoder
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

Sequence diagram for game

```mermaid

sequenceDiagram
    title Game

    participant C as Client
    participant S as Server
    participant O as Other Clients

    Note left of S: Server: Server starts

    Note left of C: Client: Client starts

    C->>S: Connect request

    S-->>C: Success(Matches)/Failure
    Note left of C: Client: Update view

    alt Create game
        C->>S: Nick, max players
        S-->>C: Success/Failure
        Note left of C: Client: Update view
        S->>O: New Match
        Note right of O: Other clients: Update view
    else Join game
        C->>S: Nick, IP
        S-->>C: Success/Failure
        Note left of C: Client: Update view
        S->>O: New player in game
        Note right of O: Other clients: Update view
    end

    Note left of S: Server: Wait for all players

    Note left of S: Server: Game start

    Note left of S: Server: Initialize decks

    S->>C: 4 visible card IDs<br/>2 covered card IDs
    Note left of C: Client: Update model and view

    S->>C: 1 ID
    Note left of C: Client: Got starter card

    opt Flip card
        Note left of C: Client: Update model and view
    end


    Note left of C: Client: Place starter card
    C->>S: ID, side
    Note left of S: Server: Update model
    S-->>C: Success/Failure
    S->>O: New view state    
    
    Note right of O: Other clients: Update model and view

    Note left of S: Server: Wait for all players

    S->>C: token
    Note left of C: Client: Set token
    S-->>O: New view state

    S->>C: 3 IDs
    Note left of C: Client: Fill hand
    S-->>O: New view state

    S->>C: 2 IDs
    Note left of C: Client: Common objectives

    S->>C: 2 IDs
    Note left of C: Client: Secret objective options

    Note left of C: Client: Choose secret objective
    C-->>S: 1 ID

    Note left of S: Server: Wait for all players

    S->>C: bool
    Note left of C: Client: Is first player?

    S->>C: msg
    Note left of S: Server: Start game (turns)

    S->>C: state
    Note left of C: Client: Change state

    S->>O: nick
    Note right of O: Other clients: See curr player's turn

    alt Invalid place
        Note left of C: Client: Place
        C->>S: ID, side, pos
        S-->>C: fail
    else Valid place
        Note left of C: Client: Place
        C->>S: ID, side, pos
        S-->>C: success
        S->>O: New state
        Note right of O: Other clients: Update model and view
        Note left of S: Server: Update game state
    end

    alt Invalid draw
        Note left of C: Client: Draw
        C->>S: ID
        S-->>C: fail
    else Valid draw
        Note left of C: Client: Draw
        C->>S: ID
        S-->>C: success
        S-->>C: 6 IDs
        Note left of C: Client: Update view and model
        S->>O: 6 IDs
        Note right of O: Other clients: Update model and view
        Note left of S: Server: Update game state
    end

    Note left of S: Server: If END_GAME
    Note left of S: Server: Determine ranking
    S->>C: Ranking
    
```

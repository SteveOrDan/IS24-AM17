sequenceDiagram
    title Turn action

    participant C as Client
    participant S as Server
    participant O as Other Clients

    Note left of S: Server: Initialize decks

    S->>C: 4 visible card IDs<br/>2 covered card IDs
    Note left of C: Client: Update model and view

    S->>C: 1 ID
    Note left of C: Client: Got starter card

    opt Flip card
        Note left of C: Client: Update model and view
    end


    Note left of C: Client: Place card
    C->>S: ID, side
    Note left of S: Server: Update model
    S-->>O: New view state    
    
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
    Note left of S: Server: Start game

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
        S->>O: New state
        Note right of O: Other clients: Update model and view
        Note left of S: Server: Update game state
    end

    Note left of S: Server: If END_GAME
    Note left of S: Server: Determine ranking
    S->>C: Ranking

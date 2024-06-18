# IS24-AM17

![Codex Naturalis](/assets/CodexNaturalis.jpg)

## Team
- Abdallah Alkhetiar: [@Zero3474](https://github.com/Zero3474)<br>
- Daniel Bonardi: [@SteveOrDan](https://github.com/SteveOrDan)<br>
- Caterina Cardenas: [@caterinacardenasestrada](https://github.com/caterinacardenasestrada)<br>
- Simone Ceccherini: [@Simone-Cecche](https://github.com/Simone-Cecche)<br>

## Implemented functionalities

<table>
<tr><td>

| Functionality                |       Status       |
|:-----------------------------|:------------------:|
| Basic rules                  | :heavy_check_mark: |
| Complete rules               | :heavy_check_mark: |
| Socket  +  RMI               | :heavy_check_mark: |
| TUI  +  GUI                  | :heavy_check_mark: |

</td><td>

| Advanced Functionality               |       Status       |
|:-------------------------------------|:------------------:|
| Multiple Matches                     | :heavy_check_mark: |
| Resilience to clients disconnections | :heavy_check_mark: |
| Chat                                 | :heavy_check_mark: |
| Persistence                          |        :x:         |

</td></tr>
</table>

## Setup

- In the [Deliverables](deliverables) folder there is a jar file that can be exceute as both Server and Client.
- To run as the Server execute the following command:
    ```shell
    > java --enable-preview -jar PF_Soft_Ing.jar server <port_number>
    ```
    - `<port_number>` is the parameter for the starting port to open the socket and RMI server.<br>

  After the execution the server will be available and the console will print the port numbers for each connection type.

- The Client can be run with the following command:
    ```shell
    > java --enable-preview -jar PF_Soft_Ing.jar client <IP> <connection_type> <interface_type>
    ```
    - `<IP>` is the server's IP to connect to can be specified during the execution.
    - `<connection_type>` is the type of connection to establish with the server. Either `socket` or `rmi`.
    - `<interface_type>` is the type of interface to run the client with. Either `TUI` or `GUI`.

## Utilized Software

* [Mermaid](https://mermaid.js.org): UML and sequence diagrams.
* [IntelliJ](https://www.jetbrains.com/idea/): Main IDE for project development.
* [JavaFX](https://openjfx.io/): Windows application design.
* [Maven](https://maven.apache.org/): Package and dependency management.

## License

[**Codex Naturalis**](https://www.craniocreations.it/prodotto/codex-naturalis) is property of [_Cranio Creations_] and all the copyrighted graphical assets used in this project were supplied by [**Politecnico di Milano**] in collaboration with their rights' holders.

[_Cranio Creations_]: https://www.craniocreations.it/
[**Politecnico di Milano**]: https://www.polimi.it/
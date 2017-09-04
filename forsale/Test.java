/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forsale;

import java.util.ArrayList;

/**
 *
 * @author MichaelAlbert
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // A null strategy - never bid, always play your top card.
        Strategy s = new Strategy() {

            @Override
            public int bid(PlayerRecord p, AuctionState a) {
                return -1;
            }

            @Override
            public Card chooseCard(PlayerRecord p, SaleState s) {
                return p.getCards().get(0);
            }
            
        };
        
        // A random strategy - make a random bid up to your amount remaining,
        // choose a rand card to sell.
        Strategy r = new Strategy() {

            @Override
            public int bid(PlayerRecord p, AuctionState a) {
                return (int) (1 + (Math.random()*p.getCash()));
            }

            @Override
            public Card chooseCard(PlayerRecord p, SaleState s) {
                return p.getCards().get((int) (Math.random()*p.getCards().size()));
            }
            
        };

        Strategy charlie = new Strategy() {
            boolean space_station; //30
            boolean skyscraper; //29
            boolean castle; //28
            boolean palace; //27
            boolean fort; //26
            boolean manor; //25

            int i = 0;
            int x = 1;

            @Override
            public int bid(PlayerRecord p, AuctionState a) {
                getCards(a);
                int max_bid = getMaxBid(a);
                System.out.println("MAX BID: " + max_bid);

                return (int) (1 + (Math.random()*p.getCash()));
            }

            public int getMaxBid(AuctionState a) {
                // Available methods.
                // a.getCurrentBid();
                // a.getPlayersInAuction();
                // a.getPlayers();
                // a.getCardsInDeck();
                ArrayList<Card> auction = a.getCardsInAuction();
                System.out.println(auction.toString());
                System.out.println(auction.size());
                int max_card = 0;
                int min_card = 30;
                for(Card c: auction) {

                    if(c.getQuality() >= max_card) {
                        max_card = c.getQuality();
                    }
                    if (c.getQuality() < min_card) {
                        min_card = c.getQuality();
                    }
                }
                int range = max_card - min_card;
                System.out.println("MAX: " + max_card);
                System.out.println("MIN: " + min_card);
                System.out.println("RANGE: " + range);

                // Return the maximum we want to bid at any time for an auction
                if (range <= 10) {
                    return 4;
                }
                else if (range <= 15) {
                    return 5;
                } else if (range <= 20) {
                    return 6;
                } else {
                    return 7;
                }
            }   

            /**
             * Get the current round and bid round for cards in play.
             */ 
            public void getCards(AuctionState a) {
                ArrayList<Card> auction = a.getCardsInAuction();
                if (auction.size() == 6) {
                    this.i++;
                    this.x = 1;
                }
                //System.out.println("CARDS IN THE AUCTION: " + auction.toString());
                for(Card c: auction) {
                    System.out.print("Round " +i + " bid: " + x + ":\t"+ c.toString() + " " + c.getQuality() + "\n");
                }
                this.x++;
                System.out.println("");
            }


            @Override
            public Card chooseCard(PlayerRecord p, SaleState s) {
                return p.getCards().get((int) (Math.random()*p.getCards().size()));
            }
            
        };
        
        ArrayList<Player> players = new ArrayList<Player>();
        for(int i = 0; i < 2; i++) {
            players.add(new Player("CHARLIE"+ ((char) ('A' + i)), charlie));
            players.add(new Player("N" + ((char) ('A' + i)), s));
            players.add(new Player("R"+ ((char) ('A' + i)), r));
        }
        GameManager g = new GameManager(players);
        g.run();
       // System.out.println(g.getLog());
    }

}

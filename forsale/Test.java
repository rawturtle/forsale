/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forsale;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

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
            int i = 0;
            int x = 1;
            int round = 1;

            @Override
            public int bid(PlayerRecord p, AuctionState a) {
                // Return our bid amount
                return ourBid(getMaxBid(a), a, p);
            }

            public int ourBid(int max_bid, AuctionState a, PlayerRecord p) {
                if (a.getCurrentBid() == 0) {
                    return 2;
                    // If we have enough cash and we can bid. Do it.
                } else if (a.getCurrentBid() < max_bid && p.getCash() >= max_bid) {
                    return a.getCurrentBid() + 1;
                } else {
                    return 0;
                }
            }

            public int getMaxBid(AuctionState a) {
                ArrayList<Card> auction = a.getCardsInAuction();
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

                //Return the maximum we want to bid at any time for an auction
                if (range <= 10) {
                    return 3;
                }
                else if (range <= 15) {
                    return 4;
                } else if (range <= 20) {
                    return 5;
                } else {
                    return 6;
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
                for(Card c: auction) {
                    System.out.print("Round " +i + " bid: " + x + ":\t"+ c.toString() + " " + c.getQuality() + "\n");
                }
                this.x++;
                System.out.println("");
            }


            @Override
            public Card chooseCard(PlayerRecord p, SaleState s) {

                //System.out.println("\nStart of round " + p.getCash());
                sort_cards(p, s);
                //print_state(p, s);
                //System.out.println("\n----------------\n");

                // If we see a 15 bid our highest card
                if (s.getChequesAvailable().size()-1 == 15 || s.getChequesAvailable().size()-1 == 14) {
                    return (p.getCards().get(p.getCards().size()-1));
                }

                ArrayList<Integer> availableCheques = s.getChequesAvailable();
                int max_cheque = 0;
                int min_cheque = 15;
                for(int cheque: availableCheques) {

                    if(cheque >= max_cheque) {
                        max_cheque = cheque;
                    }
                    if (cheque < min_cheque) {
                        min_cheque = cheque;
                    }
                }
                int range = max_cheque - min_cheque;

                if (range <= 4) {
                    return p.getCards().get(0);

                } else if (range <= 8) {
                    return p.getCards().get((p.getCards().size()-1)/2);

                } else {
                    return p.getCards().get(p.getCards().size()-1);
                }
            }

            public void print_state(PlayerRecord p, SaleState s) {
                System.out.println("Round: " + round);

                String cardString = "";
                ArrayList<Card> ourCards = p.getCards();
                for (Card card : ourCards) {
                    cardString += card.toString() + ": " + card.getQuality() + ", ";
                }
                System.out.println("Our Cards: " + cardString);

                ArrayList<Integer> card = s.getChequesAvailable();
                System.out.print("Cheques in round: ");
                for (int c: card ) {
                    System.out.print(c + " ");
                }
                round++;
            }

            public void sort_cards(PlayerRecord p, SaleState s) {
                // Custom sorting method to rank our cards by their quality
                Collections.sort(p.getCards(), new Comparator<Card>() {
                    @Override public int compare(Card c1, Card c2 ) {
                        return c1.getQuality() - c2.getQuality();
                    }
                });

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
        System.out.println(g.getLog());
    }

}

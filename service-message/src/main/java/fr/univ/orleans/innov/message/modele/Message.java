package fr.univ.orleans.innov.message.modele;


public class Message {
    private Long id;
    private String texte;
    private String utilisateur;

    public Message(Long id, String texte, String utilisateur) {
        this.id = id;
        this.texte = texte;
        this.utilisateur = utilisateur;
    }

    public Message(String texte) {
        this.texte = texte;
    }

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }
}

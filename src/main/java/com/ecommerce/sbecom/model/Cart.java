package com.ecommerce.sbecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "carts")
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<CartItem> cartItems =  new ArrayList<>();

    private Double totalPrice = 0.0;
}

// Shopping carts:
// a] Session Based Carts: Cart's content stored in the user's session. If session expires, data is lost.
// b] Cookie Based Carts: Cart data is stored in cookies on the user's browser. Could be privacy concerns here.
// c] Database Based Carts: Cart data is stored on the server side, within a database. This approach is scalable, secure
// and allows for advanced features like cart recovery, detailed analytics and cross device accessibility.
package com.test.task.bom

import javax.persistence.*

@Entity
@Table(name = "USERS")
data class User(
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_generator")
    @SequenceGenerator(name = "users_id_generator", sequenceName = "USERS_SEQ", allocationSize = 1)
    var id: Long = 0,

    @Column(name = "USERNAME", nullable = false, unique = true)
    var username: String = "",

    @Column(name = "PASSWORD", nullable = false)
    var password: String = "",

    @Column(name = "IS_ADMINISTRATOR", nullable = false)
    var isAdministrator: Boolean = false,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var tasks: MutableList<Task> = mutableListOf()
) {
    constructor() : this(0, "", "", false)
}

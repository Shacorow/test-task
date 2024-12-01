package com.test.task.bom

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "TASK")
data class Task(
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_generator")
    @SequenceGenerator(name = "task_id_generator", sequenceName = "TASK_SEQ", allocationSize = 1)
    var id: Long,

    @Column(name = "TITLE", nullable = false, unique = true)
    var title: String,

    @Column(name = "DESCRIPTION", nullable = false)
    var description: String,

    @Column(name = "DUE_DATE", nullable = false)
    var dueDate: Date,

    @Column(name = "PRIORITY", nullable = false)
    var priority: Priority,

    @Column(name = "STATUS_TASK", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: Status,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "USERS_ID", nullable = false)
    var user: User
) {
    constructor() : this(0, "", "", Date(), Priority.LOW, Status.NOT_DONE, User())
}

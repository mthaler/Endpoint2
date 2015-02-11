package endpoint2

import akka.actor.{Actor, ActorContext, ActorPath, ActorRef}

final case class TypedActorRef[T](actorRef: ActorRef) extends AnyVal with java.lang.Comparable[ActorRef] with Serializable {

  /**
   * Returns the path for this actor (from this actor up to the root actor).
   */
  def path: ActorPath = actorRef.path

  /**
   * Comparison only takes address into account.
   */
  def compareTo(other: ActorRef) = this.path compareTo other.path

  /**
   * Java API: Sends the specified message to the sender, i.e. fire-and-forget
   * semantics, including the sender reference if possible (pass `null` if
   * there is nobody to reply to).
   *
   * <pre>
   * actor.tell(message, context);
   * </pre>
   */
  def tell(msg: T, sender: ActorRef) {
    actorRef.tell(msg, sender)
  }

  /**
   * Forwards the message and passes the original sender actor as the sender.
   *
   * Works with '!' and '?'/'ask'.
   */
  def forward(message: T)(implicit context: ActorContext) = actorRef.forward(message)(context)

  /**
   * Is the actor shut down?
   * The contract is that if this method returns true, then it will never be false again.
   * But you cannot rely on that it is alive if it returns false, since this by nature is a racy method.
   */
  @deprecated("Use context.watch(actor) and receive Terminated(actor)", "2.2") def isTerminated: Boolean = actorRef.isTerminated

  override def toString = "Actor[%s]".format(path)

  /**
   * Sends a one-way asynchronous message. E.g. fire-and-forget semantics.
   * <p/>
   *
   * If invoked from within an actor then the actor reference is implicitly passed on as the implicit 'sender' argument.
   * <p/>
   *
   * This actor 'sender' reference is then available in the receiving actor in the 'sender' member variable,
   * if invoked from within an Actor. If not then no sender is available.
   * <pre>
   *   actor ! message
   * </pre>
   * <p/>
   */
  def !(message: T)(implicit sender: ActorRef = Actor.noSender) {
    actorRef.!(message)(sender)
  }
}


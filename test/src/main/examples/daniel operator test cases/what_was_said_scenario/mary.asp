world {
	said(youLied).
	said(youOwe).
	said(husbandWifeScandal).
} view->Coroner {
	son(lawrence).
	son(john).
	married(john).
	married(alfred).
	argued(X) :- said(youOwe), son(X).
	argued(X) :- said(husbandWifeScandal), married(X).
}


export interface acct {
  id: string,
  name?: string,
  balance?: number,
  currency?: string,
  createdAt?: string,
  lastUpdatedAt?: string,
  cardType?: string
}

export interface reckoner {
  id?: string,
  inOut?: number,
  amount?: number,
  currency?: string,
  fromAcctObj?: acct,
  toAcctObj?: acct,
  transDate?: string,
  createdAt?: string
}
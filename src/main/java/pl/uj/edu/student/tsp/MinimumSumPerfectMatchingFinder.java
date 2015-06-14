package pl.uj.edu.student.tsp;

/**
 * Created by alanhawrot
 */
public class MinimumSumPerfectMatchingFinder {

    public void minSumMatching(int n, double weight[][], int sol[]) {
        int nn, i, j, head, min, max, sub, idxa, idxc;
        int kk1, kk3, kk6, mm1, mm2, mm3, mm4, mm5;
        int index = 0, idxb = 0, idxd = 0, idxe = 0, kk2 = 0, kk4 = 0, kk5 = 0;
        int aux1[] = new int[n + (n / 2) + 1];
        int aux2[] = new int[n + (n / 2) + 1];
        int aux3[] = new int[n + (n / 2) + 1];
        int aux4[] = new int[n + 1];
        int aux5[] = new int[n + 1];
        int aux6[] = new int[n + 1];
        int aux7[] = new int[n + 1];
        int aux8[] = new int[n + 1];
        int aux9[] = new int[n + 1];
        double big, eps, cswk, cwk2, cst, cstlow, xcst, xwork, xwk2, xwk3, value;
        double work1[] = new double[n + 1];
        double work2[] = new double[n + 1];
        double work3[] = new double[n + 1];
        double work4[] = new double[n + 1];
        double cost[] = new double[n * (n - 1) / 2 + 1];
        boolean fin, skip;
        // initialization
        eps = 1.0e-5;
        fin = false;
        nn = 0;
        for (j = 2; j <= n; j++)
            for (i = 1; i < j; i++) {
                nn++;
                cost[nn] = weight[i][j];
            }
        big = 1.;
        for (i = 1; i <= n; i++)
            big += cost[i];
        aux1[2] = 0;
        for (i = 3; i <= n; i++)
            aux1[i] = aux1[i - 1] + i - 2;
        head = n + 2;
        for (i = 1; i <= n; i++) {
            aux2[i] = i;
            aux3[i] = i;
            aux4[i] = 0;
            aux5[i] = i;
            aux6[i] = head;
            aux7[i] = head;
            aux8[i] = head;
            sol[i] = head;
            work1[i] = big;
            work2[i] = 0.;
            work3[i] = 0.;
            work4[i] = big;
        }
        // start procedure
        for (i = 1; i <= n; i++)
            if (sol[i] == head) {
                nn = 0;
                cwk2 = big;
                for (j = 1; j <= n; j++) {
                    min = i;
                    max = j;
                    if (i != j) {
                        if (i > j) {
                            max = i;
                            min = j;
                        }
                        sub = aux1[max] + min;
                        xcst = cost[sub];
                        cswk = cost[sub] - work2[j];
                        if (cswk <= cwk2) {
                            if (cswk == cwk2) {
                                if (nn == 0)
                                    if (sol[j] == head) nn = j;
                                continue;
                            }
                            cwk2 = cswk;
                            nn = 0;
                            if (sol[j] == head) nn = j;
                        }
                    }
                }
                if (nn != 0) {
                    work2[i] = cwk2;
                    sol[i] = nn;
                    sol[nn] = i;
                }
            }
        // initial labeling
        nn = 0;
        for (i = 1; i <= n; i++)
            if (sol[i] == head) {
                nn++;
                aux6[i] = 0;
                work4[i] = 0.;
                xwk2 = work2[i];
                for (j = 1; j <= n; j++) {
                    min = i;
                    max = j;
                    if (i != j) {
                        if (i > j) {
                            max = i;
                            min = j;
                        }
                        sub = aux1[max] + min;
                        xcst = cost[sub];
                        cswk = cost[sub] - xwk2 - work2[j];
                        if (cswk < work1[j]) {
                            work1[j] = cswk;
                            aux4[j] = i;
                        }
                    }
                }
            }
        if (nn <= 1) fin = true;
        // examine the labeling and prepare for the next step
        iterate:
        while (true) {
            if (fin) {
                // generate the original graph by expanding all shrunken blossoms
                skip = false;
                value = 0.;
                for (i = 1; i <= n; i++)
                    if (aux2[i] == i) {
                        if (aux6[i] >= 0) {
                            kk5 = sol[i];
                            kk2 = aux2[kk5];
                            kk4 = sol[kk2];
                            aux6[i] = -1;
                            aux6[kk2] = -1;
                            min = kk4;
                            max = kk5;
                            if (kk4 != kk5) {
                                if (kk4 > kk5) {
                                    max = kk4;
                                    min = kk5;
                                }
                                sub = aux1[max] + min;
                                xcst = cost[sub];
                                value += xcst;
                            }
                        }
                    }
                for (i = 1; i <= n; i++) {
                    while (true) {
                        idxb = aux2[i];
                        if (idxb == i) break;
                        mm2 = aux3[idxb];
                        idxd = aux4[mm2];
                        kk3 = mm2;
                        xwork = work4[mm2];
                        do {
                            mm1 = mm2;
                            idxe = aux5[mm1];
                            xwk2 = work2[mm1];
                            while (true) {
                                aux2[mm2] = mm1;
                                work3[mm2] -= xwk2;
                                if (mm2 == idxe) break;
                                mm2 = aux3[mm2];
                            }
                            mm2 = aux3[idxe];
                            aux3[idxe] = mm1;
                        } while (mm2 != idxd);
                        work2[idxb] = xwork;
                        aux3[idxb] = idxd;
                        mm2 = idxd;
                        while (true) {
                            work3[mm2] -= xwork;
                            if (mm2 == idxb) break;
                            mm2 = aux3[mm2];
                        }
                        mm5 = sol[idxb];
                        mm1 = aux2[mm5];
                        mm1 = sol[mm1];
                        kk1 = aux2[mm1];
                        if (idxb != kk1) {
                            sol[kk1] = mm5;
                            kk3 = aux7[kk1];
                            kk3 = aux2[kk3];
                            do {
                                mm3 = aux6[kk1];
                                kk2 = aux2[mm3];
                                mm1 = aux7[kk2];
                                mm2 = aux8[kk2];
                                kk1 = aux2[mm1];
                                sol[kk1] = mm2;
                                sol[kk2] = mm1;
                                min = mm1;
                                max = mm2;
                                if (mm1 == mm2) {
                                    skip = true;
                                    break;
                                }
                                if (mm1 > mm2) {
                                    max = mm1;
                                    min = mm2;
                                }
                                sub = aux1[max] + min;
                                xcst = cost[sub];
                                value += xcst;
                            } while (kk1 != idxb);
                            if (kk3 == idxb) skip = true;
                        }
                        if (skip)
                            skip = false;
                        else {
                            while (true) {
                                kk5 = aux6[kk3];
                                kk2 = aux2[kk5];
                                kk6 = aux6[kk2];
                                min = kk5;
                                max = kk6;
                                if (kk5 == kk6) break;
                                if (kk5 > kk6) {
                                    max = kk5;
                                    min = kk6;
                                }
                                sub = aux1[max] + min;
                                xcst = cost[sub];
                                value += xcst;
                                kk6 = aux7[kk2];
                                kk3 = aux2[kk6];
                                if (kk3 == idxb) break;
                            }
                        }
                    }
                }
                weight[0][0] = value;
                return;
            }
            cstlow = big;
            for (i = 1; i <= n; i++)
                if (aux2[i] == i) {
                    cst = work1[i];
                    if (aux6[i] < head) {
                        cst = 0.5 * (cst + work4[i]);
                        if (cst <= cstlow) {
                            index = i;
                            cstlow = cst;
                        }
                    } else {
                        if (aux7[i] < head) {
                            if (aux3[i] != i) {
                                cst += work2[i];
                                if (cst < cstlow) {
                                    index = i;
                                    cstlow = cst;
                                }
                            }
                        } else {
                            if (cst < cstlow) {
                                index = i;
                                cstlow = cst;
                            }
                        }
                    }
                }
            if (aux7[index] >= head) {
                skip = false;
                if (aux6[index] < head) {
                    idxd = aux4[index];
                    idxe = aux5[index];
                    kk4 = index;
                    kk1 = kk4;
                    kk5 = aux2[idxd];
                    kk2 = kk5;
                    while (true) {
                        aux7[kk1] = kk2;
                        mm5 = aux6[kk1];
                        if (mm5 == 0) break;
                        kk2 = aux2[mm5];
                        kk1 = aux7[kk2];
                        kk1 = aux2[kk1];
                    }
                    idxb = kk1;
                    kk1 = kk5;
                    kk2 = kk4;
                    while (true) {
                        if (aux7[kk1] < head) break;
                        aux7[kk1] = kk2;
                        mm5 = aux6[kk1];
                        if (mm5 == 0) {
                            // augmentation of the matching
                            // exchange the matching and non-matching edges
                            // along the augmenting path
                            idxb = kk4;
                            mm5 = idxd;
                            while (true) {
                                kk1 = idxb;
                                while (true) {
                                    sol[kk1] = mm5;
                                    mm5 = aux6[kk1];
                                    aux7[kk1] = head;
                                    if (mm5 == 0) break;
                                    kk2 = aux2[mm5];
                                    mm1 = aux7[kk2];
                                    mm5 = aux8[kk2];
                                    kk1 = aux2[mm1];
                                    sol[kk2] = mm1;
                                }
                                if (idxb != kk4) break;
                                idxb = kk5;
                                mm5 = idxe;
                            }
                            // remove all labels on on-exposed base nodes
                            for (i = 1; i <= n; i++)
                                if (aux2[i] == i) {
                                    if (aux6[i] < head) {
                                        cst = cstlow - work4[i];
                                        work2[i] += cst;
                                        aux6[i] = head;
                                        if (sol[i] != head)
                                            work4[i] = big;
                                        else {
                                            aux6[i] = 0;
                                            work4[i] = 0.;
                                        }
                                    } else {
                                        if (aux7[i] < head) {
                                            cst = work1[i] - cstlow;
                                            work2[i] += cst;
                                            aux7[i] = head;
                                            aux8[i] = head;
                                        }
                                        work4[i] = big;
                                    }
                                    work1[i] = big;
                                }
                            nn -= 2;
                            if (nn <= 1) {
                                fin = true;
                                continue iterate;
                            }
                            // determine the new work1 values
                            for (i = 1; i <= n; i++) {
                                kk1 = aux2[i];
                                if (aux6[kk1] == 0) {
                                    xwk2 = work2[kk1];
                                    xwk3 = work3[i];
                                    for (j = 1; j <= n; j++) {
                                        kk2 = aux2[j];
                                        if (kk1 != kk2) {
                                            min = i;
                                            max = j;
                                            if (i != j) {
                                                if (i > j) {
                                                    max = i;
                                                    min = j;
                                                }
                                                sub = aux1[max] + min;
                                                xcst = cost[sub];
                                                cswk = cost[sub] - xwk2 - xwk3;
                                                cswk -= (work2[kk2] + work3[j]);
                                                if (cswk < work1[kk2]) {
                                                    aux4[kk2] = i;
                                                    aux5[kk2] = j;
                                                    work1[kk2] = cswk;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            continue iterate;
                        }
                        kk2 = aux2[mm5];
                        kk1 = aux7[kk2];
                        kk1 = aux2[kk1];
                    }
                    while (true) {
                        if (kk1 == idxb) {
                            skip = true;
                            break;
                        }
                        mm5 = aux7[idxb];
                        aux7[idxb] = head;
                        idxa = sol[mm5];
                        idxb = aux2[idxa];
                    }
                }
                if (!skip) {
                    // growing an alternating tree, add two edges
                    aux7[index] = aux4[index];
                    aux8[index] = aux5[index];
                    idxa = sol[index];
                    idxc = aux2[idxa];
                    work4[idxc] = cstlow;
                    aux6[idxc] = sol[idxc];
                    msmSubprogramb(idxc, n, big, cost, aux1, aux2, aux3, aux4,
                            aux5, aux7, aux9, work1, work2, work3, work4);
                    continue;
                }
                skip = false;
                // shrink a blossom
                xwork = work2[idxb] + cstlow - work4[idxb];
                work2[idxb] = 0.;
                mm1 = idxb;
                do {
                    work3[mm1] += xwork;
                    mm1 = aux3[mm1];
                } while (mm1 != idxb);
                mm5 = aux3[idxb];
                if (idxb == kk5) {
                    kk5 = kk4;
                    kk2 = aux7[idxb];
                }
                while (true) {
                    aux3[mm1] = kk2;
                    idxa = sol[kk2];
                    aux6[kk2] = idxa;
                    xwk2 = work2[kk2] + work1[kk2] - cstlow;
                    mm1 = kk2;
                    do {
                        mm2 = mm1;
                        work3[mm2] += xwk2;
                        aux2[mm2] = idxb;
                        mm1 = aux3[mm2];
                    } while (mm1 != kk2);
                    aux5[kk2] = mm2;
                    work2[kk2] = xwk2;
                    kk1 = aux2[idxa];
                    aux3[mm2] = kk1;
                    xwk2 = work2[kk1] + cstlow - work4[kk1];
                    mm2 = kk1;
                    do {
                        mm1 = mm2;
                        work3[mm1] += xwk2;
                        aux2[mm1] = idxb;
                        mm2 = aux3[mm1];
                    } while (mm2 != kk1);
                    aux5[kk1] = mm1;
                    work2[kk1] = xwk2;
                    if (kk5 != kk1) {
                        kk2 = aux7[kk1];
                        aux7[kk1] = aux8[kk2];
                        aux8[kk1] = aux7[kk2];
                        continue;
                    }
                    if (kk5 != index) {
                        aux7[kk5] = idxe;
                        aux8[kk5] = idxd;
                        if (idxb != index) {
                            kk5 = kk4;
                            kk2 = aux7[idxb];
                            continue;
                        }
                    } else {
                        aux7[index] = idxd;
                        aux8[index] = idxe;
                    }
                    break;
                }
                aux3[mm1] = mm5;
                kk4 = aux3[idxb];
                aux4[kk4] = mm5;
                work4[kk4] = xwork;
                aux7[idxb] = head;
                work4[idxb] = cstlow;
                msmSubprogramb(idxb, n, big, cost, aux1, aux2, aux3, aux4,
                        aux5, aux7, aux9, work1, work2, work3, work4);
                continue iterate;
            }
            // expand a t-labeled blossom
            kk4 = aux3[index];
            kk3 = kk4;
            idxd = aux4[kk4];
            mm2 = kk4;
            do {
                mm1 = mm2;
                idxe = aux5[mm1];
                xwk2 = work2[mm1];
                while (true) {
                    aux2[mm2] = mm1;
                    work3[mm2] -= xwk2;
                    if (mm2 == idxe) break;
                    mm2 = aux3[mm2];
                }
                mm2 = aux3[idxe];
                aux3[idxe] = mm1;
            } while (mm2 != idxd);
            xwk2 = work4[kk4];
            work2[index] = xwk2;
            aux3[index] = idxd;
            mm2 = idxd;
            while (true) {
                work3[mm2] -= xwk2;
                if (mm2 == index) break;
                mm2 = aux3[mm2];
            }
            mm1 = sol[index];
            kk1 = aux2[mm1];
            mm2 = aux6[kk1];
            idxb = aux2[mm2];
            if (idxb != index) {
                kk2 = idxb;
                while (true) {
                    mm5 = aux7[kk2];
                    kk1 = aux2[mm5];
                    if (kk1 == index) break;
                    kk2 = aux6[kk1];
                    kk2 = aux2[kk2];
                }
                aux7[idxb] = aux7[index];
                aux7[index] = aux8[kk2];
                aux8[idxb] = aux8[index];
                aux8[index] = mm5;
                mm3 = aux6[idxb];
                kk3 = aux2[mm3];
                mm4 = aux6[kk3];
                aux6[idxb] = head;
                sol[idxb] = mm1;
                kk1 = kk3;
                while (true) {
                    mm1 = aux7[kk1];
                    mm2 = aux8[kk1];
                    aux7[kk1] = mm4;
                    aux8[kk1] = mm3;
                    aux6[kk1] = mm1;
                    sol[kk1] = mm1;
                    kk2 = aux2[mm1];
                    sol[kk2] = mm2;
                    mm3 = aux6[kk2];
                    aux6[kk2] = mm2;
                    if (kk2 == index) break;
                    kk1 = aux2[mm3];
                    mm4 = aux6[kk1];
                    aux7[kk2] = mm3;
                    aux8[kk2] = mm4;
                }
            }
            mm2 = aux8[idxb];
            kk1 = aux2[mm2];
            work1[kk1] = cstlow;
            kk4 = 0;
            skip = false;
            if (kk1 != idxb) {
                mm1 = aux7[kk1];
                kk3 = aux2[mm1];
                aux7[kk1] = aux7[idxb];
                aux8[kk1] = mm2;
                do {
                    mm5 = aux6[kk1];
                    aux6[kk1] = head;
                    kk2 = aux2[mm5];
                    mm5 = aux7[kk2];
                    aux7[kk2] = head;
                    kk5 = aux8[kk2];
                    aux8[kk2] = kk4;
                    kk4 = kk2;
                    work4[kk2] = cstlow;
                    kk1 = aux2[mm5];
                    work1[kk1] = cstlow;
                } while (kk1 != idxb);
                aux7[idxb] = kk5;
                aux8[idxb] = mm5;
                aux6[idxb] = head;
                if (kk3 == idxb) skip = true;
            }
            if (skip)
                skip = false;
            else {
                kk1 = 0;
                kk2 = kk3;
                do {
                    mm5 = aux6[kk2];
                    aux6[kk2] = head;
                    aux7[kk2] = head;
                    aux8[kk2] = kk1;
                    kk1 = aux2[mm5];
                    mm5 = aux7[kk1];
                    aux6[kk1] = head;
                    aux7[kk1] = head;
                    aux8[kk1] = kk2;
                    kk2 = aux2[mm5];
                } while (kk2 != idxb);
                msmSubprograma(kk1, n, big, cost, aux1, aux2, aux3, aux4, aux5,
                        aux6, aux8, work1, work2, work3, work4);
            }
            while (true) {
                if (kk4 == 0) continue iterate;
                idxb = kk4;
                msmSubprogramb(idxb, n, big, cost, aux1, aux2, aux3, aux4,
                        aux5, aux7, aux9, work1, work2, work3, work4);
                kk4 = aux8[idxb];
                aux8[idxb] = head;
            }
        }
    }

    private void msmSubprograma(int kk, int n, double big,
                                double cost[], int aux1[], int aux2[], int aux3[],
                                int aux4[], int aux5[], int aux6[], int aux8[],
                                double work1[], double work2[], double work3[],
                                double work4[]) {
 /* this method is used internally by minSumMatching */
        int i, head, j, jj1, jj2, jj3, jj4, min, max, sub;
        double cswk, cstwk, xcst, xwk2, xwk3;
        head = n + 2;
        do {
            jj1 = kk;
            kk = aux8[jj1];
            aux8[jj1] = head;
            cstwk = big;
            jj3 = 0;
            jj4 = 0;
            j = jj1;
            xwk2 = work2[jj1];
            do {
                xwk3 = work3[j];
                for (i = 1; i <= n; i++) {
                    jj2 = aux2[i];
                    if (aux6[jj2] < head) {
                        min = j;
                        max = i;
                        if (j != i) {
                            if (j > i) {
                                max = j;
                                min = i;
                            }
                            sub = aux1[max] + min;
                            xcst = cost[sub];
                            cswk = cost[sub] - xwk2 - xwk3;
                            cswk -= (work2[jj2] + work3[i]);
                            cswk += work4[jj2];
                            if (cswk < cstwk) {
                                jj3 = i;
                                jj4 = j;
                                cstwk = cswk;
                            }
                        }
                    }
                }
                j = aux3[j];
            } while (j != jj1);
            aux4[jj1] = jj3;
            aux5[jj1] = jj4;
            work1[jj1] = cstwk;
        } while (kk != 0);
    }

    private void msmSubprogramb(int kk, int n, double big,
                                double cost[], int aux1[], int aux2[], int aux3[],
                                int aux4[], int aux5[], int aux7[], int aux9[],
                                double work1[], double work2[], double work3[],
                                double work4[]) {
 /* this method is used internally by minSumMatching */
        int i, ii, head, jj1, jj2, jj3, min, max, sub;
        double cswk, xcst, xwk1, xwk2;
        head = n + 2;
        xwk1 = work4[kk] - work2[kk];
        work1[kk] = big;
        xwk2 = xwk1 - work3[kk];
        aux7[kk] = 0;
        ii = 0;
        for (i = 1; i <= n; i++) {
            jj3 = aux2[i];
            if (aux7[jj3] >= head) {
                ii++;
                aux9[ii] = i;
                min = kk;
                max = i;
                if (kk != i) {
                    if (kk > i) {
                        max = kk;
                        min = i;
                    }
                    sub = aux1[max] + min;
                    cswk = cost[sub] + xwk2;
                    cswk -= (work2[jj3] + work3[i]);
                    if (cswk < work1[jj3]) {
                        aux4[jj3] = kk;
                        aux5[jj3] = i;
                        work1[jj3] = cswk;
                    }
                }
            }
        }
        aux7[kk] = head;
        jj1 = kk;
        jj1 = aux3[jj1];
        if (jj1 == kk) return;
        do {
            xwk2 = xwk1 - work3[jj1];
            for (i = 1; i <= ii; i++) {
                jj2 = aux9[i];
                jj3 = aux2[jj2];
                min = jj1;
                max = jj2;
                if (jj1 != jj2) {
                    if (jj1 > jj2) {
                        max = jj1;
                        min = jj2;
                    }
                    sub = aux1[max] + min;
                    xcst = cost[sub];
                    cswk = cost[sub] + xwk2;
                    cswk -= (work2[jj3] + work3[jj2]);
                    if (cswk < work1[jj3]) {
                        aux4[jj3] = jj1;
                        aux5[jj3] = jj2;
                        work1[jj3] = cswk;
                    }
                }
            }
            jj1 = aux3[jj1];
        } while (jj1 != kk);
    }
}

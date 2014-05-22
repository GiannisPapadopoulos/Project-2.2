poisson = importdata('poisson');
uniform = importdata('uniform');

% 1: distance, 2: time, 3: speed, 4 : % of time spent on traffic lights
row = 2;

lengths = [length(poisson), length(uniform)];
M = [mean(poisson(row:row,:)), mean(uniform(row:row,:))];
stdDevs = [std(poisson(row:row,:)), std(uniform(row:row,:))];
confidenceInterval = zeros(1, length(M));
for i=1:length(M)
    confidenceInterval(i) = 1.96*stdDevs(i) / sqrt(lengths(i));
end

errorbar(M, confidenceInterval);
yL = ylim;
yL(1) = 0;
yL(2) = yL(2) * 1.1;
ylim(yL);